package tn.nightbeam.chronofall.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.core.ChronoConfig;
import tn.nightbeam.chronofall.core.Flux;
import tn.nightbeam.chronofall.item.FluxItem;
import tn.nightbeam.chronofall.item.RobotItem;

/**
 * A small hovering helper bound to the player who deployed it. It follows its owner at shoulder height; chips add
 * behaviour: Collector (brings items within range to the owner), Guard (shoots hostile mobs near the owner) and Lamp
 * (lights its surroundings). It runs on Flux and powers down when empty.
 */
public class DroneEntity extends PathfinderMob implements Packable, FluxHolder {
    public static final List<String> CHIPS = List.of("collector_chip", "guard_chip", "lamp_chip");
    private static final EntityDataAccessor<Integer> FLUX = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> CHIP_BITS = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.BYTE);
    private static final int SHOT_COOLDOWN = 20;

    @Nullable
    private UUID owner;
    @Nullable
    private BlockPos light;
    private int shotCooldown;
    @Nullable
    private ItemEntity fetching;

    public DroneEntity(EntityType<? extends DroneEntity> type, Level level) {
        super(type, level);
        setNoGravity(true);
        setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FLUX, 0);
        builder.define(CHIP_BITS, (byte) 0);
    }

    @Override
    public int flux() {
        return entityData.get(FLUX);
    }

    @Override
    public int fluxCapacity() {
        return Flux.DRONE_CAPACITY;
    }

    @Override
    public void setFlux(int value) {
        entityData.set(FLUX, Math.max(0, Math.min(fluxCapacity(), value)));
    }

    public boolean hasChip(String chip) {
        return (entityData.get(CHIP_BITS) & (1 << CHIPS.indexOf(chip))) != 0;
    }

    private void setChip(String chip, boolean on) {
        int bit = 1 << CHIPS.indexOf(chip);
        int bits = entityData.get(CHIP_BITS);
        entityData.set(CHIP_BITS, (byte) (on ? bits | bit : bits & ~bit));
    }

    public boolean poweredDown() {
        return flux() <= 0;
    }

    @Nullable
    public Player getOwner() {
        return owner == null ? null : level().getPlayerByUUID(owner);
    }

    public void setOwner(@Nullable Player player) {
        owner = player == null ? null : player.getUUID();
    }

    @Override
    public void tick() {
        super.tick();
        setNoGravity(!poweredDown());
        if (level().isClientSide) {
            if (!poweredDown() && random.nextInt(3) == 0) {
                level().addParticle(ModContent.FLUX_PARTICLE.get(), getX() + random.triangle(0, 0.2), getY() - 0.05, getZ() + random.triangle(0, 0.2),
                        0, -0.03, 0);
            }
            return;
        }
        if (isNoAi()) {
            return;
        }
        if (poweredDown()) {
            setDeltaMovement(getDeltaMovement().multiply(0.6, 1.0, 0.6));
            clearLight();
            return;
        }
        setFlux(flux() - Flux.DRONE_PER_TICK);
        Player player = getOwner();
        if (player == null || player.isSpectator()) {
            hover(position(), 0.0, false);
            clearLight();
            return;
        }
        if (distanceToSqr(player) > 32 * 32) {
            Vec3 at = shoulder(player);
            teleportTo(at.x, at.y, at.z);
        }
        if (shotCooldown > 0) {
            shotCooldown--;
        }
        int range = ChronoConfig.get().robots().droneRange();
        Vec3 goal = shoulder(player);
        boolean aiming = false;
        if (hasChip("guard_chip")) {
            LivingEntity enemy = findEnemy(player, range);
            if (enemy != null) {
                aiming = true;
                lookAt(enemy, 360, 360);
                yBodyRot = getYRot();
                yHeadRot = getYRot();
                if (shotCooldown == 0 && hasLineOfSight(enemy) && flux() >= Flux.DRONE_PER_SHOT) {
                    shoot(enemy);
                }
            }
        }
        if (hasChip("collector_chip")) {
            ItemEntity item = findItem(player, range);
            if (item != null) {
                goal = item.position().add(0, 0.4, 0);
                if (distanceToSqr(item) < 1.6 * 1.6) {
                    deliver(player, item);
                }
            }
        }
        hover(goal, 0.12 * Math.sin(tickCount * 0.15), aiming);
        if (hasChip("lamp_chip") && ChronoConfig.get().robots().droneLampLight()) {
            updateLight();
        } else {
            clearLight();
        }
        if (random.nextInt(400) == 0) {
            playSound(ModContent.sound("drone_beep"), 0.5F, 1.0F + random.nextFloat() * 0.3F);
        }
    }

    /** Above the owner's right shoulder, slightly behind. */
    private static Vec3 shoulder(Player player) {
        Vec3 right = Vec3.directionFromRotation(0, player.getYRot() + 90).scale(-0.9);
        Vec3 back = Vec3.directionFromRotation(0, player.getYRot()).scale(-0.4);
        return player.position().add(right).add(back).add(0, player.getBbHeight() + 0.35, 0);
    }

    private void hover(Vec3 goal, double bob, boolean aiming) {
        Vec3 delta = goal.add(0, bob, 0).subtract(position());
        double distance = delta.length();
        Vec3 wanted = distance < 0.05 ? Vec3.ZERO : delta.normalize().scale(Math.min(0.7, distance * 0.18));
        setDeltaMovement(getDeltaMovement().scale(0.7).add(wanted.scale(0.3)));
        if (distance > 0.3 && !aiming) {
            setYRot((float) Math.toDegrees(Math.atan2(-delta.x, delta.z)));
            yBodyRot = getYRot();
            yHeadRot = getYRot();
        }
    }

    @Override
    public void travel(Vec3 input) {
        if (isNoGravity()) {
            move(net.minecraft.world.entity.MoverType.SELF, getDeltaMovement());
            calculateEntityAnimation(true);
        } else {
            super.travel(input);
        }
    }

    @Nullable
    private LivingEntity findEnemy(Player player, int range) {
        AABB area = player.getBoundingBox().inflate(range);
        return level().getEntitiesOfClass(LivingEntity.class, area, e -> e instanceof Enemy && e.isAlive() && !e.isInvisible()
                        && !(e instanceof DroneEntity))
                .stream().min(Comparator.comparingDouble(player::distanceToSqr)).orElse(null);
    }

    @Nullable
    private ItemEntity findItem(Player player, int range) {
        if (fetching != null && fetching.isAlive() && fetching.distanceToSqr(player) < (range + 2) * (range + 2)) {
            return fetching;
        }
        AABB area = player.getBoundingBox().inflate(range);
        fetching = level().getEntitiesOfClass(ItemEntity.class, area, e -> e.isAlive() && !e.getItem().isEmpty() && e.tickCount > 20)
                .stream().min(Comparator.comparingDouble(this::distanceToSqr)).orElse(null);
        return fetching;
    }

    private void deliver(Player player, ItemEntity item) {
        ItemStack stack = item.getItem().copy();
        int before = stack.getCount();
        player.getInventory().add(stack);
        if (stack.isEmpty()) {
            item.discard();
        } else {
            item.setItem(stack);
        }
        if (stack.getCount() != before) {
            level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.3F, 1.6F);
        }
        fetching = null;
    }

    private void shoot(LivingEntity enemy) {
        Vec3 from = position().add(0, 0.25, 0).add(getLookAngle().scale(0.4));
        Vec3 aim = enemy.getBoundingBox().getCenter().subtract(from);
        LaserBoltEntity.fire(level(), this, from, aim, 4.0F, LaserBoltEntity.CYAN);
        playSound(ModContent.sound("laser_shot"), 0.6F, 1.5F);
        setFlux(flux() - Flux.DRONE_PER_SHOT);
        shotCooldown = SHOT_COOLDOWN;
    }

    private void updateLight() {
        BlockPos pos = blockPosition();
        if (pos.equals(light)) {
            return;
        }
        clearLight();
        if (level().getBlockState(pos).isAir()) {
            level().setBlock(pos, Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 14), 3);
            light = pos;
        }
    }

    private void clearLight() {
        if (light != null) {
            if (level().getBlockState(light).is(Blocks.LIGHT)) {
                level().setBlock(light, Blocks.AIR.defaultBlockState(), 3);
            }
            light = null;
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!level().isClientSide) {
            clearLight();
        }
        super.remove(reason);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (owner != null && !player.getUUID().equals(owner) && !player.getAbilities().instabuild) {
            return InteractionResult.PASS;
        }
        ItemStack held = player.getItemInHand(hand);
        String path = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(held.getItem()).getPath();
        if (level().isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (owner == null) {
            setOwner(player);
        }
        if (CHIPS.contains(path) && held.getItem() == ModContent.item(path)) {
            if (hasChip(path)) {
                player.displayClientMessage(Component.translatable("message.chronofall.drone.has_chip", held.getHoverName()), true);
            } else {
                setChip(path, true);
                held.consume(1, player);
                playSound(SoundEvents.ARMOR_EQUIP_NETHERITE.value(), 1.0F, 1.4F);
                player.displayClientMessage(Component.translatable("message.chronofall.drone.chip", held.getHoverName()), true);
            }
            return InteractionResult.CONSUME;
        }
        if (FluxHolder.useCell(this, player, held)) {
            return InteractionResult.CONSUME;
        }
        if (player.isSecondaryUseActive() && held.isEmpty()) {
            player.setItemInHand(hand, RobotItem.packed((RobotItem) ModContent.item("drone"), pack()));
            playSound(SoundEvents.IRON_GOLEM_REPAIR, 0.8F, 1.6F);
            discard();
            return InteractionResult.CONSUME;
        }
        player.displayClientMessage(status(), true);
        return InteractionResult.CONSUME;
    }

    private Component status() {
        List<String> chips = new ArrayList<>();
        for (String chip : CHIPS) {
            if (hasChip(chip)) {
                chips.add(Component.translatable("item.chronofall." + chip).getString());
            }
        }
        return Component.translatable("message.chronofall.drone.status", Flux.percent(flux(), fluxCapacity()),
                chips.isEmpty() ? Component.translatable("message.chronofall.drone.no_chips").getString() : String.join(", ", chips));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity attacker = source.getEntity();
        if (attacker != null && owner != null && owner.equals(attacker.getUUID()) && !(attacker instanceof Player p && p.getAbilities().instabuild)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        spawnAtLocation(new ItemStack(ModContent.item("scrap"), 3));
        for (String chip : CHIPS) {
            if (hasChip(chip)) {
                spawnAtLocation(new ItemStack(ModContent.item(chip)));
            }
        }
    }

    @Override
    public CompoundTag pack() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(FluxItem.KEY, flux());
        for (String chip : CHIPS) {
            if (hasChip(chip)) {
                tag.putBoolean(chip, true);
            }
        }
        return tag;
    }

    @Override
    public void unpack(CompoundTag data, @Nullable Player player) {
        setFlux(data.getInt(FluxItem.KEY));
        for (String chip : CHIPS) {
            setChip(chip, data.getBoolean(chip));
        }
        setOwner(player);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.merge(pack());
        if (owner != null) {
            tag.putUUID("Owner", owner);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setFlux(tag.getInt(FluxItem.KEY));
        for (String chip : CHIPS) {
            setChip(chip, tag.getBoolean(chip));
        }
        owner = tag.hasUUID("Owner") ? tag.getUUID("Owner") : null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModContent.sound("robot_hurt");
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, net.minecraft.world.level.block.state.BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }
}
