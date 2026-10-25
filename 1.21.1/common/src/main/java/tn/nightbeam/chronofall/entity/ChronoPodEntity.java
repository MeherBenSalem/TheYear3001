package tn.nightbeam.chronofall.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.rift.RiftEvents;

/** The pod that falls out of a Chrono Rift, trailing fire and rift sparks, and crashes into the ground. */
public class ChronoPodEntity extends Entity {
    public static final double FALL_SPEED = 1.15;

    public ChronoPodEntity(EntityType<? extends ChronoPodEntity> type, Level level) {
        super(type, level);
        setNoGravity(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 motion = getDeltaMovement();
        if (motion.y > -FALL_SPEED) {
            motion = new Vec3(motion.x * 0.98, Math.max(-FALL_SPEED, motion.y - 0.08), motion.z * 0.98);
            setDeltaMovement(motion);
        }
        if (level().isClientSide) {
            for (int i = 0; i < 6; i++) {
                double t = random.nextDouble();
                double x = getX() - motion.x * t + random.triangle(0, 0.35);
                double y = getY() + 0.9 - motion.y * t;
                double z = getZ() - motion.z * t + random.triangle(0, 0.35);
                level().addParticle(i % 3 == 0 ? ModContent.RIFT_PARTICLE.get() : ParticleTypes.FLAME, x, y, z, 0, 0.05, 0);
                if (i % 2 == 0) {
                    level().addParticle(ParticleTypes.LARGE_SMOKE, x, y + 0.6, z, 0, 0.08, 0);
                }
            }
            return;
        }
        move(MoverType.SELF, getDeltaMovement());
        if (onGround() || verticalCollision || horizontalCollision || getY() < level().getMinBuildHeight() + 2) {
            // The ground block the pod hit: the first solid block at or below its base (a fast pod can end its last
            // move a little inside the ground).
            BlockPos impact = BlockPos.containing(getX(), getY() + 0.5, getZ());
            while (level().getBlockState(impact).getCollisionShape(level(), impact).isEmpty() && impact.getY() > getY() - 3) {
                impact = impact.below();
            }
            if (level() instanceof ServerLevel server) {
                RiftEvents.impact(server, impact);
            }
            discard();
        }
        if (tickCount > 600) {
            discard();
        }
    }

    /** Spin for the renderer: the pod turns slowly as it falls. */
    public float spin(float partialTick) {
        return (tickCount + partialTick) * 9.0F;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 256 * 256;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float amount) {
        return false;
    }
}
