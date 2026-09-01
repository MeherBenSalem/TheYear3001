package tn.nightbeam.robotica.entity;

import tn.nightbeam.robotica.procedures.CheckIdleAnimProcedure;
import tn.nightbeam.robotica.procedures.CheckAttackAnimProcedure;
import tn.nightbeam.robotica.init.RoboticaModItems;
import tn.nightbeam.robotica.init.RoboticaModEntities;
import tn.nightbeam.robotica.init.RoboticaModSounds;


import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;

public class SentinelEntity extends Monster {
	public final AnimationState animationState0 = new AnimationState();
	public final AnimationState animationState2 = new AnimationState();


	public SentinelEntity(EntityType<SentinelEntity> type, Level world) {
		super(type, world);		xpReward = 0;
		setNoAi(false);
	}


	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.8));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, Player.class, false, false));
		this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, Villager.class, false, false));
	}

	protected void dropCustomDeathLoot(net.minecraft.server.level.ServerLevel serverLevel, DamageSource source, boolean recentlyHitIn) {
		super.dropCustomDeathLoot(serverLevel, source, recentlyHitIn);
		this.spawnAtLocation(new ItemStack(RoboticaModItems.SCRAP.get()));
	}

	@Override
	public SoundEvent getAmbientSound() {
		return RoboticaModSounds.ALIENT_ROBOT_AMBIENT.get();
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return net.minecraft.sounds.SoundEvents.GENERIC_HURT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return net.minecraft.sounds.SoundEvents.GENERIC_DEATH;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			this.animationState0.animateWhen(CheckIdleAnimProcedure.execute(this), this.tickCount);
			this.animationState2.animateWhen(CheckAttackAnimProcedure.execute(this), this.tickCount);
		}
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.25);
		builder = builder.add(Attributes.MAX_HEALTH, 30);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 5);
		builder = builder.add(Attributes.FOLLOW_RANGE, 32);
		return builder;
	}
}