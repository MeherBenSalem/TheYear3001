package tn.nightbeam.nextmillennium.procedures;

import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.api.ScaleOperations;

import tn.nightbeam.nextmillennium.init.Theyear3001ModItems;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;

public class UpgradeCardsProcedure {
	public static void execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Theyear3001ModItems.REPAIR_CARD.get()) {
			if (entity instanceof LivingEntity _entity)
				_entity.setHealth((float) ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) + 10));
			if (sourceentity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(Theyear3001ModItems.REPAIR_CARD.get());
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
		} else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Theyear3001ModItems.BULK_UPGRADE_CARD.get()) {
			if (entity instanceof LivingEntity _livingEntity9 && _livingEntity9.getAttributes().hasAttribute(Attributes.MAX_HEALTH))
				_livingEntity9.getAttribute(Attributes.MAX_HEALTH)
						.setBaseValue(((entity instanceof LivingEntity _livingEntity7 && _livingEntity7.getAttributes().hasAttribute(Attributes.MAX_HEALTH) ? _livingEntity7.getAttribute(Attributes.MAX_HEALTH).getValue() : 0)
								+ Mth.nextInt(RandomSource.create(), 1, 5)));
			if (sourceentity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(("Upgraded " + entity.getDisplayName().getString() + " Max Health : "
						+ (entity instanceof LivingEntity _livingEntity11 && _livingEntity11.getAttributes().hasAttribute(Attributes.MAX_HEALTH) ? _livingEntity11.getAttribute(Attributes.MAX_HEALTH).getValue() : 0))), false);
			ScaleTypes.HEIGHT.getScaleData(entity).setTargetScale((float) ScaleOperations.ADD.applyAsDouble(ScaleTypes.HEIGHT.getScaleData(entity).getTargetScale(), 0.05));
			ScaleTypes.WIDTH.getScaleData(entity).setTargetScale((float) ScaleOperations.ADD.applyAsDouble(ScaleTypes.WIDTH.getScaleData(entity).getTargetScale(), 0.05));
			if (entity instanceof LivingEntity _entity)
				_entity.setHealth((float) (entity instanceof LivingEntity _livingEntity15 && _livingEntity15.getAttributes().hasAttribute(Attributes.MAX_HEALTH) ? _livingEntity15.getAttribute(Attributes.MAX_HEALTH).getValue() : 0));
			if (sourceentity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(Theyear3001ModItems.BULK_UPGRADE_CARD.get());
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
		} else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Theyear3001ModItems.DAMAGE_UPGRADE_CARD.get()) {
			if (entity instanceof LivingEntity _livingEntity22 && _livingEntity22.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE))
				_livingEntity22.getAttribute(Attributes.ATTACK_DAMAGE)
						.setBaseValue(((entity instanceof LivingEntity _livingEntity20 && _livingEntity20.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? _livingEntity20.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0)
								+ Mth.nextDouble(RandomSource.create(), 0.5, 2)));
			if (sourceentity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(("Upgraded " + entity.getDisplayName().getString() + " damage to "
						+ (entity instanceof LivingEntity _livingEntity24 && _livingEntity24.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? _livingEntity24.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0))), false);
			if (sourceentity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(Theyear3001ModItems.DAMAGE_UPGRADE_CARD.get());
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
		} else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Theyear3001ModItems.SPEED_UPGRADE_CARD.get()) {
			if (entity instanceof LivingEntity _livingEntity31 && _livingEntity31.getAttributes().hasAttribute(Attributes.MOVEMENT_SPEED))
				_livingEntity31.getAttribute(Attributes.MOVEMENT_SPEED)
						.setBaseValue(((entity instanceof LivingEntity _livingEntity29 && _livingEntity29.getAttributes().hasAttribute(Attributes.MOVEMENT_SPEED) ? _livingEntity29.getAttribute(Attributes.MOVEMENT_SPEED).getValue() : 0)
								+ Mth.nextDouble(RandomSource.create(), 0.01, 0.03)));
			if (sourceentity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(("Upgraded " + entity.getDisplayName().getString() + " movement speed to "
						+ (entity instanceof LivingEntity _livingEntity33 && _livingEntity33.getAttributes().hasAttribute(Attributes.MOVEMENT_SPEED) ? _livingEntity33.getAttribute(Attributes.MOVEMENT_SPEED).getValue() : 0))), false);
			if (sourceentity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(Theyear3001ModItems.SPEED_UPGRADE_CARD.get());
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
		}
	}
}
