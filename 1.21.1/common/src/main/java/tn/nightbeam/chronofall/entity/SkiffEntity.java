package tn.nightbeam.chronofall.entity;

import org.jetbrains.annotations.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import tn.nightbeam.chronofall.Chronofall;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.core.Flux;
import tn.nightbeam.chronofall.item.FluxItem;
import tn.nightbeam.chronofall.item.RobotItem;

/**
 * A one-seat hover craft. The rider's client flies it (as vanilla does for boats): it turns toward where the rider
 * looks, W/S accelerate and brake, jump climbs and sprint descends. Speed builds and decays smoothly and the hull banks
 * into turns. With no rider or no Flux it settles to the ground. It never counts as "floating" for the server's
 * flying check because it has no gravity of its own.
 */
public class SkiffEntity extends Entity implements Packable, FluxHolder {
    public static final double MAX_SPEED = 0.95;
    public static final double CLIMB = 0.32;
    private static final EntityDataAccessor<Integer> FLUX = SynchedEntityData.defineId(SkiffEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HITS = SynchedEntityData.defineId(SkiffEntity.class, EntityDataSerializers.INT);

    private double speed;
    private double climb;
    private float bank;
    private float bankO;
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    /** Server: position at the previous tick (the rider's client moves the skiff between ticks). */
    private Vec3 lastPos;

    public SkiffEntity(EntityType<? extends SkiffEntity> type, Level level) {
        super(type, level);
        setNoGravity(true);
        blocksBuilding = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(FLUX, 0);
        builder.define(HITS, 0);
    }

    @Override
    public int flux() {
        return entityData.get(FLUX);
    }

    @Override
    public int fluxCapacity() {
        return Flux.SKIFF_CAPACITY;
    }

    @Override
    public void setFlux(int value) {
        entityData.set(FLUX, Math.max(0, Math.min(fluxCapacity(), value)));
    }

    /** Roll of the hull in degrees for rendering. */
    public float bank(float partialTick) {
        return Mth.lerp(partialTick, bankO, bank);
    }

    /** Current forward speed in blocks per tick (for the HUD). */
    public double speed() {
        return speed;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return getFirstPassenger() instanceof Player player ? player : null;
    }

    @Override
    public void tick() {
        super.tick();
        bankO = bank;
        tickLerp();
        if (isControlledByLocalInstance()) {
            if (getControllingPassenger() instanceof Player rider && flux() > 0) {
                fly(rider);
            } else {
                settle();
            }
            move(MoverType.SELF, getDeltaMovement());
            if (horizontalCollision) {
                speed *= 0.5;
            }
        }
        if (level().isClientSide) {
            clientEffects();
        } else {
            if (lastPos != null && isVehicle() && position().distanceToSqr(lastPos) > 0.02 * 0.02) {
                setFlux(flux() - Flux.SKIFF_PER_TICK);
            }
            lastPos = position();
            if (entityData.get(HITS) > 0 && tickCount % 40 == 0) {
                entityData.set(HITS, entityData.get(HITS) - 1);
            }
        }
    }

    private void fly(Player rider) {
        float target = rider.getYRot();
        float turn = Mth.clamp(Mth.wrapDegrees(target - getYRot()), -6.0F, 6.0F);
        setYRot(getYRot() + turn);
        yRotO = getYRot();
        bank = Mth.lerp(0.2F, bank, -turn * 4.0F);
        double input = rider.zza;
        if (input > 0) {
            speed = Math.min(MAX_SPEED, speed + 0.035);
        } else if (input < 0) {
            speed = Math.max(-0.2, speed - 0.05);
        } else {
            speed *= 0.985;
        }
        boolean up = Chronofall.client().jumpHeld();
        boolean down = Chronofall.client().descendHeld();
        double wanted = up ? CLIMB : down ? -CLIMB : 0.02 * Math.sin(tickCount * 0.1);
        climb = Mth.lerp(0.15, climb, wanted);
        Vec3 heading = Vec3.directionFromRotation(0, getYRot());
        Vec3 strafe = Vec3.directionFromRotation(0, getYRot() - 90).scale(rider.xxa * 0.12);
        setDeltaMovement(heading.x * speed + strafe.x, climb, heading.z * speed + strafe.z);
    }

    private void settle() {
        bank = Mth.lerp(0.1F, bank, 0);
        speed *= 0.9;
        climb = Math.max(-0.5, climb - 0.03);
        if (onGround()) {
            climb = 0;
            speed = 0;
        }
        Vec3 heading = Vec3.directionFromRotation(0, getYRot());
        setDeltaMovement(heading.x * speed, climb, heading.z * speed);
    }

    private void clientEffects() {
        if (isVehicle()) {
            Chronofall.client().loopSound(this, "skiff");
        }
        if (!isVehicle() || flux() <= 0) {
            return;
        }
        Vec3 back = Vec3.directionFromRotation(0, getYRot()).scale(-1.25);
        Vec3 side = Vec3.directionFromRotation(0, getYRot() + 90).scale(0.88);
        int count = 1 + (int) (Math.abs(speed) * 3);
        for (int s = -1; s <= 1; s += 2) {
            Vec3 nozzle = position().add(back).add(side.scale(s)).add(0, 0.42, 0);
            for (int i = 0; i < count; i++) {
                level().addParticle(ModContent.THRUSTER_PARTICLE.get(), nozzle.x + random.triangle(0, 0.08), nozzle.y + random.triangle(0, 0.08),
                        nozzle.z + random.triangle(0, 0.08), back.x * 0.08, -0.01, back.z * 0.08);
            }
        }
        if (random.nextInt(3) == 0) {
            level().addParticle(ModContent.FLUX_PARTICLE.get(), getX() + random.triangle(0, 0.6), getY() + 0.05, getZ() + random.triangle(0, 0.6), 0, -0.05, 0);
        }
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
        lerpX = x;
        lerpY = y;
        lerpZ = z;
        lerpYRot = yRot;
        lerpSteps = 10;
    }

    private void tickLerp() {
        if (isControlledByLocalInstance()) {
            lerpSteps = 0;
            syncPacketPositionCodec(getX(), getY(), getZ());
        }
        if (lerpSteps > 0) {
            float oldYaw = getYRot();
            lerpPositionAndRotationStep(lerpSteps, lerpX, lerpY, lerpZ, lerpYRot, 0);
            lerpSteps--;
            bank = Mth.lerp(0.2F, bank, -Mth.clamp(Mth.wrapDegrees(getYRot() - oldYaw), -6.0F, 6.0F) * 4.0F);
        }
    }

    @Override
    public double lerpTargetX() {
        return lerpSteps > 0 ? lerpX : getX();
    }

    @Override
    public double lerpTargetY() {
        return lerpSteps > 0 ? lerpY : getY();
    }

    @Override
    public double lerpTargetZ() {
        return lerpSteps > 0 ? lerpZ : getZ();
    }

    @Override
    public float lerpTargetYRot() {
        return lerpSteps > 0 ? (float) lerpYRot : getYRot();
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
        return new Vec3(0, 0.62, 0.05).yRot(-getYRot() * Mth.DEG_TO_RAD);
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return getPassengers().isEmpty();
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (level().isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (FluxHolder.useCell(this, player, held)) {
            return InteractionResult.CONSUME;
        }
        if (player.isSecondaryUseActive() && held.isEmpty() && !isVehicle()) {
            player.setItemInHand(hand, RobotItem.packed((RobotItem) ModContent.item("skiff"), pack()));
            playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, 1.2F);
            discard();
            return InteractionResult.CONSUME;
        }
        if (!isVehicle()) {
            player.startRiding(this);
            if (flux() <= 0) {
                player.displayClientMessage(Component.translatable("message.chronofall.no_flux"), true);
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (level().isClientSide || isRemoved() || isInvulnerableTo(source)) {
            return false;
        }
        if (source.getEntity() instanceof Player player && !hasPassenger(player)) {
            int hits = entityData.get(HITS) + 1;
            entityData.set(HITS, hits);
            playSound(SoundEvents.IRON_GOLEM_HURT, 0.8F, 1.4F);
            if (hits >= 3 || player.getAbilities().instabuild) {
                if (!player.getAbilities().instabuild) {
                    spawnAtLocation(RobotItem.packed((RobotItem) ModContent.item("skiff"), pack()));
                }
                ejectPassengers();
                discard();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isPickable() {
        return !isRemoved();
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public CompoundTag pack() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(FluxItem.KEY, flux());
        return tag;
    }

    @Override
    public void unpack(CompoundTag data, @Nullable Player player) {
        setFlux(data.getInt(FluxItem.KEY));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt(FluxItem.KEY, flux());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setFlux(tag.getInt(FluxItem.KEY));
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModContent.item("skiff"));
    }
}
