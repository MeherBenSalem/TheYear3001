package tn.nightbeam.chronofall.entity;

import org.jspecify.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import tn.nightbeam.chronofall.Chronofall;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.core.ChronoConfig;
import tn.nightbeam.chronofall.core.Flux;
import tn.nightbeam.chronofall.core.UpgradeRules;
import tn.nightbeam.chronofall.item.FluxItem;
import tn.nightbeam.chronofall.item.RobotItem;

/**
 * A rideable walker. It steps up full blocks, leaps on jump (hold to charge, like a horse), and landing from three
 * blocks or more slams the ground: damage and knockback to mobs around it. While seated, the rider hits harder and
 * reaches further. Upgrade cards add fixed, capped bonuses (see {@link UpgradeRules}).
 */
public class MechEntity extends PathfinderMob implements PlayerRideableJumping, Packable, FluxHolder {
    private static final EntityDataAccessor<Integer> FLUX = SynchedEntityData.defineId(MechEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SLAM = SynchedEntityData.defineId(MechEntity.class, EntityDataSerializers.INT);
    private static final net.minecraft.resources.Identifier RIDER_DAMAGE = Chronofall.id("mech_rider_damage");
    private static final net.minecraft.resources.Identifier RIDER_REACH = Chronofall.id("mech_rider_reach");

    private final CompoundTag upgrades = new CompoundTag();
    private float pendingJump;
    /** Server: position at the previous tick (the rider's client moves the mech between ticks). */
    private Vec3 lastPos;

    public MechEntity(EntityType<? extends MechEntity> type, Level level) {
        super(type, level);
        setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, UpgradeRules.MECH_BASE_HEALTH)
                .add(Attributes.ARMOR, UpgradeRules.MECH_BASE_ARMOR)
                .add(Attributes.MOVEMENT_SPEED, UpgradeRules.MECH_BASE_SPEED)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.STEP_HEIGHT, 1.1)
                .add(Attributes.SAFE_FALL_DISTANCE, 1000.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FLUX, 0);
        builder.define(SLAM, 0);
    }

    @Override
    public int flux() {
        return entityData.get(FLUX);
    }

    @Override
    public int fluxCapacity() {
        return Flux.MECH_CAPACITY;
    }

    @Override
    public void setFlux(int value) {
        entityData.set(FLUX, Math.max(0, Math.min(fluxCapacity(), value)));
    }

    /** Ticks left of the slam pose (arms raised), for the model. */
    public int slamTicks() {
        return entityData.get(SLAM);
    }

    public int level(UpgradeRules.Card card) {
        return upgrades.getIntOr(card.key(), 0);
    }

    // Riding -------------------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return getFirstPassenger() instanceof Player player ? player : null;
    }

    @Override
    protected void tickRidden(Player player, Vec3 input) {
        super.tickRidden(player, input);
        setYRot(player.getYRot());
        yRotO = getYRot();
        setXRot(0);
        yBodyRot = getYRot();
        yHeadRot = getYRot();
        if (pendingJump > 0 && onGround() && flux() > 0) {
            double power = 0.55 + 0.45 * pendingJump;
            Vec3 forward = Vec3.directionFromRotation(0, getYRot()).scale(0.6 * pendingJump);
            setDeltaMovement(getDeltaMovement().x + forward.x, power, getDeltaMovement().z + forward.z);
            needsSync = true;
            playSound(SoundEvents.PISTON_EXTEND, 1.0F, 0.6F);
        }
        pendingJump = 0;
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 input) {
        if (flux() <= 0) {
            return Vec3.ZERO;
        }
        float forward = player.zza;
        return new Vec3(player.xxa * 0.4F, 0, forward < 0 ? forward * 0.4F : forward);
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return (float) getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Override
    public void onPlayerJump(int power) {
        if (flux() > 0) {
            pendingJump = power >= 90 ? 1.0F : 0.4F + 0.6F * power / 90.0F;
        }
    }

    @Override
    public boolean canJump() {
        return flux() > 0;
    }

    @Override
    public void handleStartJump(int power) {
    }

    @Override
    public void handleStopJump() {
    }

    @Override
    public int getJumpCooldown() {
        return 0;
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
        return new Vec3(0, 1.62, -0.05);
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (passenger instanceof Player player) {
            applyRiderBonus(player, true);
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (passenger instanceof Player player) {
            applyRiderBonus(player, false);
        }
    }

    private void applyRiderBonus(Player player, boolean on) {
        AttributeInstance damage = player.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance reach = player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
        if (damage != null) {
            damage.removeModifier(RIDER_DAMAGE);
            if (on) {
                damage.addTransientModifier(new AttributeModifier(RIDER_DAMAGE, UpgradeRules.bonusDamage(level(UpgradeRules.Card.DAMAGE)),
                        AttributeModifier.Operation.ADD_VALUE));
            }
        }
        if (reach != null) {
            reach.removeModifier(RIDER_REACH);
            if (on) {
                reach.addTransientModifier(new AttributeModifier(RIDER_REACH, 2.0, AttributeModifier.Operation.ADD_VALUE));
            }
        }
    }

    // Ticking ------------------------------------------------------------------------------------------------------

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            return;
        }
        if (slamTicks() > 0) {
            entityData.set(SLAM, slamTicks() - 1);
        }
        if (lastPos != null && isVehicle() && position().distanceToSqr(lastPos) > 0.01 * 0.01) {
            setFlux(flux() - Flux.MECH_PER_TICK);
        }
        lastPos = position();
        if (getControllingPassenger() instanceof Player player && tickCount % 20 == 0) {
            applyRiderBonus(player, true);
        }
    }

    @Override
    public boolean causeFallDamage(double distance, float multiplier, DamageSource source) {
        if (distance >= 3.0F && !level().isClientSide() && isVehicle()) {
            slam();
        }
        return false;
    }

    /** The ground slam: damage and knockback to every living thing around except the mech and its rider. */
    private void slam() {
        if (!(level() instanceof ServerLevel server)) {
            return;
        }
        ChronoConfig.Robots cfg = ChronoConfig.get().robots();
        Entity rider = getControllingPassenger();
        DamageSource source = rider instanceof Player player ? damageSources().playerAttack(player) : damageSources().mobAttack(this);
        for (LivingEntity target : server.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(cfg.slamRadius(), 1.5, cfg.slamRadius()),
                e -> e != this && e != rider && e.isAlive() && !(e instanceof DroneEntity))) {
            Vec3 push = target.position().subtract(position()).multiply(1, 0, 1);
            target.hurtServer(server, source, (float) cfg.slamDamage());
            if (push.lengthSqr() > 1.0E-4) {
                push = push.normalize();
                target.knockback(1.2, -push.x, -push.z, source, (float) cfg.slamDamage());
            }
        }
        entityData.set(SLAM, 12);
        server.playSound(null, getX(), getY(), getZ(), ModContent.sound("mech_slam"), SoundSource.NEUTRAL, 1.5F, 0.9F + random.nextFloat() * 0.2F);
        server.sendParticles(ParticleTypes.EXPLOSION, getX(), getY() + 0.2, getZ(), 2, 0.6, 0.1, 0.6, 0.0);
        BlockState ground = level().getBlockState(getOnPos());
        for (int i = 0; i < 28; i++) {
            double a = Math.PI * 2 * i / 28;
            double r = cfg.slamRadius() * 0.8;
            server.sendParticles(ModContent.FLUX_PARTICLE.get(), getX() + Math.cos(a) * r, getY() + 0.15, getZ() + Math.sin(a) * r, 1, 0, 0, 0, 0);
            if (!ground.isAir()) {
                server.sendParticles(new net.minecraft.core.particles.BlockParticleOption(ParticleTypes.BLOCK, ground),
                        getX() + Math.cos(a) * r * 0.6, getY() + 0.1, getZ() + Math.sin(a) * r * 0.6, 3, 0.1, 0.2, 0.1, 0.15);
            }
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(ModContent.sound("mech_step"), 0.7F, 0.9F + random.nextFloat() * 0.2F);
    }

    // Interaction --------------------------------------------------------------------------------------------------

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (level().isClientSide()) {
            return isVehicle() && getFirstPassenger() != player ? InteractionResult.PASS : InteractionResult.SUCCESS;
        }
        UpgradeRules.Card card = held.isEmpty() ? null : UpgradeRules.Card.byItem(BuiltInRegistries.ITEM.getKey(held.getItem()).getPath());
        if (card != null && BuiltInRegistries.ITEM.getKey(held.getItem()).getNamespace().equals("chronofall")) {
            applyCard(player, held, card);
            return InteractionResult.CONSUME;
        }
        if (FluxHolder.useCell(this, player, held)) {
            return InteractionResult.CONSUME;
        }
        if (player.isSecondaryUseActive() && held.isEmpty()) {
            if (isVehicle()) {
                return InteractionResult.PASS;
            }
            player.setItemInHand(hand, RobotItem.packed((RobotItem) ModContent.item("mech"), pack()));
            playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, 0.8F);
            discard();
            return InteractionResult.CONSUME;
        }
        if (!isVehicle()) {
            player.startRiding(this);
            if (flux() <= 0) {
                player.sendOverlayMessage(Component.translatable("message.chronofall.no_flux"));
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    private void applyCard(Player player, ItemStack held, UpgradeRules.Card card) {
        if (card == UpgradeRules.Card.REPAIR) {
            if (getHealth() >= getMaxHealth()) {
                player.sendOverlayMessage(Component.translatable("message.chronofall.mech.healthy"));
                return;
            }
            heal((float) card.step());
            held.consume(1, player);
            playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, 1.0F);
            player.sendOverlayMessage(Component.translatable("message.chronofall.mech.repaired", (int) getHealth(), (int) getMaxHealth()));
            return;
        }
        int next = UpgradeRules.apply(card, level(card));
        if (next < 0) {
            player.sendOverlayMessage(Component.translatable("message.chronofall.mech.capped", held.getHoverName(), card.cap()));
            return;
        }
        upgrades.putInt(card.key(), next);
        applyUpgrades();
        if (card == UpgradeRules.Card.BULK) {
            heal((float) card.step());
        }
        held.consume(1, player);
        playSound(SoundEvents.ARMOR_EQUIP_NETHERITE.value(), 1.0F, 0.9F);
        player.sendOverlayMessage(Component.translatable("message.chronofall.mech.upgraded", held.getHoverName(), next, card.cap()));
        if (getControllingPassenger() instanceof Player rider) {
            applyRiderBonus(rider, true);
        }
    }

    private void applyUpgrades() {
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(UpgradeRules.maxHealth(level(UpgradeRules.Card.BULK)));
        getAttribute(Attributes.ARMOR).setBaseValue(UpgradeRules.armor(level(UpgradeRules.Card.DEFENSE)));
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(UpgradeRules.speed(level(UpgradeRules.Card.SPEED)));
        if (getHealth() > getMaxHealth()) {
            setHealth(getMaxHealth());
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (source.getEntity() != null && hasPassenger(source.getEntity())) {
            return false;
        }
        return super.hurtServer(level, source, amount);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        spawnAtLocation(level, new ItemStack(ModContent.item("scrap_block"), 2));
        spawnAtLocation(level, new ItemStack(ModContent.item("titanium_ingot"), 3));
    }

    // Saving -------------------------------------------------------------------------------------------------------

    @Override
    public CompoundTag pack() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(FluxItem.KEY, flux());
        if (!upgrades.isEmpty()) {
            tag.put("Upgrades", upgrades.copy());
        }
        tag.putFloat("Health", getHealth());
        return tag;
    }

    @Override
    public void unpack(CompoundTag data, @Nullable Player player) {
        setFlux(data.getIntOr(FluxItem.KEY, 0));
        CompoundTag saved = data.getCompoundOrEmpty("Upgrades");
        for (String key : saved.keySet()) {
            upgrades.putInt(key, saved.getIntOr(key, 0));
        }
        applyUpgrades();
        setHealth(data.contains("Health") ? Math.max(1, data.getFloatOr("Health", 0.0F)) : getMaxHealth());
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(FluxItem.KEY, flux());
        tag.store("Upgrades", CompoundTag.CODEC, upgrades.copy());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput tag) {
        super.readAdditionalSaveData(tag);
        setFlux(tag.getIntOr(FluxItem.KEY, 0));
        CompoundTag saved = tag.read("Upgrades", CompoundTag.CODEC).orElseGet(CompoundTag::new);
        for (String key : saved.keySet()) {
            upgrades.putInt(key, saved.getIntOr(key, 0));
        }
        applyUpgrades();
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
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return !isVehicle();
    }
}
