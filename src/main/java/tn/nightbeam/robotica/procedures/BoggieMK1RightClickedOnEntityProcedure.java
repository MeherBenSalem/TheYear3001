package tn.nightbeam.robotica.procedures;

import tn.nightbeam.robotica.entity.BoggieMK1Entity;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class BoggieMK1RightClickedOnEntityProcedure {
	public static void execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.COAL) {
			if (Math.random() < (1) / ((float) 10)) {
				entity.setCustomName(Component.literal((sourceentity.getDisplayName().getString() + "'s Boggie")));
				if (entity instanceof TamableAnimal _toTame && sourceentity instanceof Player _owner)
					_toTame.tame(_owner);
			}
			if (entity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(Items.COAL);
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
		} else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.RED_DYE) {
			if (entity instanceof BoggieMK1Entity animatable)
				animatable.setTexture("bot_texture_2");
		} else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.BLUE_DYE) {
			if (entity instanceof BoggieMK1Entity animatable)
				animatable.setTexture("boggie");
		} else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.YELLOW_DYE) {
			if (entity instanceof BoggieMK1Entity animatable)
				animatable.setTexture("bot_texture_3");
		} else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.GREEN_DYE) {
			if (entity instanceof BoggieMK1Entity animatable)
				animatable.setTexture("bot_texture_4");
		}
		UpgradeCardsProcedure.execute(entity, sourceentity);
	}
}
