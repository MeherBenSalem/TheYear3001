package tn.nightbeam.chronofall.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import tn.nightbeam.chronofall.content.ModContent;

/**
 * A hostile robot from the rift. It keeps its distance and fires red laser bolts (5 damage) every one and a half to
 * two seconds. Its visor glows in the dark (the renderer's emissive layer).
 */
public class SentinelEntity extends Monster implements RangedAttackMob {
    public static final float BOLT_DAMAGE = 5.0F;

    public SentinelEntity(EntityType<? extends SentinelEntity> type, Level level) {
        super(type, level);
        xpReward = 10;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.ARMOR, 4.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.4);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0, 30, 40, 14.0F));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 12.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this, SentinelEntity.class));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, DroneEntity.class, true));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide()) {
            setAggressive(getTarget() != null);
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        Vec3 side = Vec3.directionFromRotation(0, yBodyRot - 90).scale(0.45);
        Vec3 from = new Vec3(getX(), getY() + 1.45, getZ()).add(side).add(getLookAngle().scale(0.5));
        Vec3 aim = target.getEyePosition().add(0, -0.3, 0).subtract(from);
        double spread = 0.06 * (3 - level().getDifficulty().getId());
        aim = aim.normalize().add(random.triangle(0, spread), random.triangle(0, spread), random.triangle(0, spread));
        LaserBoltEntity.fire(level(), this, from, aim, BOLT_DAMAGE, LaserBoltEntity.RED);
        level().playSound(null, getX(), getY(), getZ(), ModContent.sound("laser_shot"), SoundSource.HOSTILE, 1.0F, 0.75F + random.nextFloat() * 0.1F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModContent.sound("sentinel_ambient");
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModContent.sound("sentinel_hurt");
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModContent.sound("sentinel_death");
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }
}
