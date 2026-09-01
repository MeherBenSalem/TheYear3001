package tn.nightbeam.robotica.procedures;

import tn.nightbeam.robotica.init.RoboticaModItems;

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
		if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.REPAIR_CARD.get()) {
			if (entity instanceof LivingEntity _entity)
				_entity.setHealth((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) + 10);
			if (sourceentity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(RoboticaModItems.REPAIR_CARD.get());
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
		} else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.BULK_UPGRADE_CARD.get()) {
			if (entity instanceof LivingEntity _livingEntity9 && _livingEntity9.getAttributes().hasAttribute(Attributes.MAX_HEALTH))
				_livingEntity9.getAttribute(Attributes.MAX_HEALTH)
						.setBaseValue(((entity instanceof LivingEntity _livingEntity7 && _livingEntity7.getAttributes().hasAttribute(Attributes.MAX_HEALTH) ? _livingEntity7.getAttribute(Attributes.MAX_HEALTH).getValue() : 0)
								+ Mth.nextInt(RandomSource.create(), 1, 5)));
			if (sourceentity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(("Upgraded " + entity.getDisplayName().getString() + " max health to "
						+ (entity instanceof LivingEntity _livingEntity11 && _livingEntity11.getAttributes().hasAttribute(Attributes.MAX_HEALTH) ? _livingEntity11.getAttribute(Attributes.MAX_HEALTH).getValue() : 0))), false);
			if (entity instanceof LivingEntity _entity)
				_entity.setHealth((float) (entity instanceof LivingEntity _livingEntity13 && _livingEntity13.getAttributes().hasAttribute(Attributes.MAX_HEALTH) ? _livingEntity13.getAttribute(Attributes.MAX_HEALTH).getValue() : 0));
			if (sourceentity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(RoboticaModItems.BULK_UPGRADE_CARD.get());
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
		} else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.DAMAGE_UPGRADE_CARD.get()) {
			if (entity instanceof LivingEntity _livingEntity20 && _livingEntity20.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE))
				_livingEntity20.getAttribute(Attributes.ATTACK_DAMAGE)
						.setBaseValue(((entity instanceof LivingEntity _livingEntity18 && _livingEntity18.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? _livingEntity18.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0)
								+ Mth.nextDouble(RandomSource.create(), 0.5, 2)));
			if (sourceentity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(("Upgraded " + entity.getDisplayName().getString() + " damage to "
						+ (entity instanceof LivingEntity _livingEntity22 && _livingEntity22.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? _livingEntity22.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0))), false);
			if (sourceentity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(RoboticaModItems.DAMAGE_UPGRADE_CARD.get());
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
		} else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.SPEED_UPGRADE_CARD.get()) {
			if (entity instanceof LivingEntity _livingEntity29 && _livingEntity29.getAttributes().hasAttribute(Attributes.MOVEMENT_SPEED))
				_livingEntity29.getAttribute(Attributes.MOVEMENT_SPEED)
						.setBaseValue(((entity instanceof LivingEntity _livingEntity27 && _livingEntity27.getAttributes().hasAttribute(Attributes.MOVEMENT_SPEED) ? _livingEntity27.getAttribute(Attributes.MOVEMENT_SPEED).getValue() : 0)
								+ Mth.nextDouble(RandomSource.create(), 0.01, 0.03)));
			if (sourceentity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(("Upgraded " + entity.getDisplayName().getString() + " movement speed to "
						+ (entity instanceof LivingEntity _livingEntity31 && _livingEntity31.getAttributes().hasAttribute(Attributes.MOVEMENT_SPEED) ? _livingEntity31.getAttribute(Attributes.MOVEMENT_SPEED).getValue() : 0))), false);
			if (sourceentity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(RoboticaModItems.SPEED_UPGRADE_CARD.get());
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
		} else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.DEFENSE_UPGRADE_CARD.get()) {
			if (entity instanceof LivingEntity _livingEntity38 && _livingEntity38.getAttributes().hasAttribute(Attributes.ARMOR))
				_livingEntity38.getAttribute(Attributes.ARMOR)
						.setBaseValue(((entity instanceof LivingEntity _livingEntity36 && _livingEntity36.getAttributes().hasAttribute(Attributes.ARMOR) ? _livingEntity36.getAttribute(Attributes.ARMOR).getValue() : 0)
								+ Mth.nextDouble(RandomSource.create(), 1, 3)));
			if (sourceentity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(("Upgraded " + entity.getDisplayName().getString() + " armor to "
						+ (entity instanceof LivingEntity _livingEntity40 && _livingEntity40.getAttributes().hasAttribute(Attributes.ARMOR) ? _livingEntity40.getAttribute(Attributes.ARMOR).getValue() : 0))), false);
			if (sourceentity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(RoboticaModItems.DEFENSE_UPGRADE_CARD.get());
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
		}
	}
}