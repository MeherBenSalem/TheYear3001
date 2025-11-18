package tn.nightbeam.robotica.procedures;

import tn.nightbeam.robotica.init.RoboticaModMenus;
import tn.nightbeam.robotica.init.RoboticaModItems;
import tn.nightbeam.robotica.init.RoboticaModBlocks;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class RecipeMechProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		ItemStack Result = ItemStack.EMPTY;
		Result = new ItemStack(RoboticaModItems.MECH_MK_1_EGG.get()).copy();
		if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu0 ? _menu0.getSlots().get(6).getItem() : ItemStack.EMPTY).getItem() == RoboticaModBlocks.SCRAP_BLOCK.get().asItem()
				&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu2 ? _menu2.getSlots().get(7).getItem() : ItemStack.EMPTY).getItem() == Blocks.REDSTONE_BLOCK.asItem()
				&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu4 ? _menu4.getSlots().get(8).getItem() : ItemStack.EMPTY).getItem() == RoboticaModBlocks.SCRAP_BLOCK.get()
						.asItem()) {
			if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu6 ? _menu6.getSlots().get(11).getItem() : ItemStack.EMPTY).getItem() == Blocks.COMPARATOR.asItem()
					&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu8 ? _menu8.getSlots().get(12).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.PRINTED_CIRCUIT_BOARD
							.get()
					&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu10 ? _menu10.getSlots().get(13).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.PRINTED_CIRCUIT_BOARD
							.get()) {
				if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu12 ? _menu12.getSlots().get(16).getItem() : ItemStack.EMPTY).getItem() == RoboticaModBlocks.SCRAP_BLOCK.get()
						.asItem()
						&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu14 ? _menu14.getSlots().get(17).getItem() : ItemStack.EMPTY)
								.getItem() == RoboticaModItems.ELECTRICAL_CONDUCTORS.get()
						&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu16 ? _menu16.getSlots().get(18).getItem() : ItemStack.EMPTY)
								.getItem() == RoboticaModItems.ELECTRICAL_CONDUCTORS.get()) {
					if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu18 ? _menu18.getSlots().get(20).getItem() : ItemStack.EMPTY).getItem() == Result.getItem()) {
						if (entity instanceof Player _player && _player.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu) {
							ItemStack _setstack = Result.copy();
							_setstack.setCount(new Object() {
								public int getAmount(int sltid) {
									if (entity instanceof Player player && player.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu) {
										ItemStack stack = _menu.getSlots().get(sltid).getItem();
										if (stack != null)
											return stack.getCount();
									}
									return 0;
								}
							}.getAmount(20) + 1);
							_menu.getSlots().get(20).set(_setstack);
							_player.containerMenu.broadcastChanges();
						}
						return true;
					} else if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu22 ? _menu22.getSlots().get(20).getItem() : ItemStack.EMPTY).getItem() == ItemStack.EMPTY.getItem()) {
						if (entity instanceof Player _player && _player.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu) {
							ItemStack _setstack = Result.copy();
							_setstack.setCount(1);
							_menu.getSlots().get(20).set(_setstack);
							_player.containerMenu.broadcastChanges();
						}
						return true;
					}
				}
				return false;
			}
			return false;
		}
		return false;
	}
}