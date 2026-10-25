package tn.nightbeam.chronofall.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import tn.nightbeam.chronofall.content.ModContent;

/**
 * A straight, fast laser bolt with no gravity. Drawn entirely with particles (a glowing trail), so it looks the same on
 * every renderer. Cyan bolts come from players and drones, red ones from Sentinels.
 */
public class LaserBoltEntity extends ThrowableProjectile {
    public static final int CYAN = 0;
    public static final int RED = 1;
    public static final double SPEED = 1.8;
    private static final int LIFETIME = 50;
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(LaserBoltEntity.class, EntityDataSerializers.INT);

    private float damage = 5.0F;

    public LaserBoltEntity(EntityType<? extends LaserBoltEntity> type, Level level) {
        super(type, level);
        setNoGravity(true);
    }

    public static LaserBoltEntity fire(Level level, Entity owner, Vec3 from, Vec3 direction, float damage, int color) {
        LaserBoltEntity bolt = new LaserBoltEntity(ModContent.LASER_BOLT.get(), level);
        bolt.setOwner(owner);
        bolt.setPos(from.x, from.y, from.z);
        bolt.setDeltaMovement(direction.normalize().scale(SPEED));
        ProjectileUtil.rotateTowardsMovement(bolt, 1.0F);
        bolt.damage = damage;
        bolt.entityData.set(COLOR, color);
        level.addFreshEntity(bolt);
        return bolt;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(COLOR, CYAN);
    }

    public int color() {
        return entityData.get(COLOR);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    public void tick() {
        Vec3 before = position();
        super.tick();
        if (isRemoved()) {
            return;
        }
        if (level().isClientSide) {
            Vec3 step = position().subtract(before);
            for (int i = 0; i < 4; i++) {
                Vec3 p = before.add(step.scale(i / 4.0));
                level().addParticle(color() == RED ? ModContent.LASER_PARTICLE.get() : ModContent.FLUX_PARTICLE.get(), p.x, p.y + 0.12, p.z, 0, 0, 0);
            }
        } else if (tickCount > LIFETIME || isInWater()) {
            discard();
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        if (!super.canHitEntity(target)) {
            return false;
        }
        Entity owner = getOwner();
        if (owner instanceof DroneEntity drone && (target == drone.getOwner() || target instanceof DroneEntity)) {
            return false;
        }
        if (target instanceof DroneEntity drone && owner != null && owner == drone.getOwner()) {
            return false;
        }
        return !(owner instanceof SentinelEntity && target instanceof SentinelEntity);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        Entity owner = getOwner();
        target.hurt(damageSources().mobProjectile(this, owner instanceof LivingEntity living ? living : null), damage);
        impact(result.getLocation());
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        impact(result.getLocation());
    }

    private void impact(Vec3 at) {
        if (level() instanceof ServerLevel server) {
            server.sendParticles(color() == RED ? ModContent.LASER_PARTICLE.get() : ModContent.FLUX_PARTICLE.get(), at.x, at.y, at.z, 8, 0.1, 0.1, 0.1, 0.08);
            server.sendParticles(ParticleTypes.SMOKE, at.x, at.y, at.z, 2, 0.05, 0.05, 0.05, 0.01);
            server.playSound(null, at.x, at.y, at.z, ModContent.sound("laser_hit"), SoundSource.NEUTRAL, 0.6F, 1.2F + random.nextFloat() * 0.3F);
        }
        discard();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 96 * 96;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Damage", damage);
        tag.putInt("Color", color());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        damage = tag.getFloat("Damage");
        entityData.set(COLOR, tag.getInt("Color"));
    }
}
