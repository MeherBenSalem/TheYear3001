package tn.nightbeam.robotica.entity;

import tn.nightbeam.robotica.procedures.CheckIdleAnimProcedure;
import tn.nightbeam.robotica.procedures.CheckAttackAnimProcedure;
import tn.nightbeam.robotica.procedures.BoggieMK1RightClickedOnEntityProcedure;
import tn.nightbeam.robotica.init.RoboticaModItems;
import tn.nightbeam.robotica.init.RoboticaModEntities;
import tn.nightbeam.robotica.init.RoboticaModSounds;


import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.core.BlockPos;

public class BoggieEntity extends Monster {
	public final AnimationState animationState0 = new AnimationState();
	public final AnimationState animationState2 = new AnimationState();


	public BoggieEntity(EntityType<BoggieEntity> type, Level world) {
		super(type, world);		xpReward = 0;
		setNoAi(false);
	}


	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.getNavigation().getNodeEvaluator().setCanOpenDoors(true);
		this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1));
		this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(6, new FloatGoal(this));
		this.goalSelector.addGoal(7, new MeleeAttackGoal(this, 1.2, false));
		this.targetSelector.addGoal(8, new HurtByTargetGoal(this).setAlertOthers());
		this.goalSelector.addGoal(9, new OpenDoorGoal(this, true));
		this.goalSelector.addGoal(10, new OpenDoorGoal(this, false));
	}

	protected void dropCustomDeathLoot(net.minecraft.server.level.ServerLevel serverLevel, DamageSource source, boolean recentlyHitIn) {
		super.dropCustomDeathLoot(serverLevel, source, recentlyHitIn);
		this.spawnAtLocation(new ItemStack(RoboticaModItems.SCRAP.get()));
	}

	@Override
	public SoundEvent getAmbientSound() {
		return RoboticaModSounds.ROBOT_AMBIENT.get();
	}

	@Override
	public void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(RoboticaModSounds.ROBOT_DIE.get(), 0.15f, 1);
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
	public InteractionResult mobInteract(Player sourceentity, InteractionHand hand) {
		ItemStack itemstack = sourceentity.getItemInHand(hand);
		InteractionResult retval = InteractionResult.sidedSuccess(this.level().isClientSide());
		super.mobInteract(sourceentity, hand);
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		Entity entity = this;
		Level world = this.level();

		BoggieMK1RightClickedOnEntityProcedure.execute(entity, sourceentity);
		return retval;
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
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 10);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		return builder;
	}
}