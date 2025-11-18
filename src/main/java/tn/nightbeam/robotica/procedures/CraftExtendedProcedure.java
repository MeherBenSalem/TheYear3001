package tn.nightbeam.robotica.procedures;

import tn.nightbeam.robotica.init.RoboticaModMenus;
import tn.nightbeam.robotica.init.RoboticaModItems;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class CraftExtendedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		ItemStack resultItem = ItemStack.EMPTY;
		if (CheckCircleScrapProcedure.execute(entity)) {
			if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu0 ? _menu0.getSlots().get(6).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.ELECTRICAL_CONDUCTORS.get()
					&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu2 ? _menu2.getSlots().get(7).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.PRINTED_CIRCUIT_BOARD
							.get()
					&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu4 ? _menu4.getSlots().get(8).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.ELECTRICAL_CONDUCTORS
							.get()) {
				if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu6 ? _menu6.getSlots().get(11).getItem() : ItemStack.EMPTY).getItem() == Blocks.REDSTONE_BLOCK.asItem()
						&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu8 ? _menu8.getSlots().get(12).getItem() : ItemStack.EMPTY)
								.getItem() == RoboticaModItems.CENTRAL_PROCESSING_UNIT.get()
						&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu10 ? _menu10.getSlots().get(13).getItem() : ItemStack.EMPTY).getItem() == Blocks.REDSTONE_BLOCK.asItem()) {
					RemoveItemsFromCraftingGridProcedure.execute(entity);
					if (entity instanceof Player _player && _player.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu) {
						ItemStack _setstack = new ItemStack(RoboticaModItems.MECH_MK_1_EGG.get()).copy();
						_setstack.setCount(1);
						_menu.getSlots().get(20).set(_setstack);
						_player.containerMenu.broadcastChanges();
					}
				} else if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu13 ? _menu13.getSlots().get(11).getItem() : ItemStack.EMPTY)
						.getItem() == RoboticaModItems.PRINTED_CIRCUIT_BOARD.get()
						&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu15 ? _menu15.getSlots().get(12).getItem() : ItemStack.EMPTY)
								.getItem() == RoboticaModItems.CENTRAL_PROCESSING_UNIT.get()
						&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu17 ? _menu17.getSlots().get(13).getItem() : ItemStack.EMPTY)
								.getItem() == RoboticaModItems.PRINTED_CIRCUIT_BOARD.get()) {
					RemoveItemsFromCraftingGridProcedure.execute(entity);
					if (entity instanceof Player _player && _player.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu) {
						ItemStack _setstack = new ItemStack(RoboticaModItems.FLYING_CAPSULE.get()).copy();
						_setstack.setCount(1);
						_menu.getSlots().get(20).set(_setstack);
						_player.containerMenu.broadcastChanges();
					}
				} else if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu20 ? _menu20.getSlots().get(11).getItem() : ItemStack.EMPTY).getItem() == Blocks.COAL_BLOCK.asItem()
						&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu22 ? _menu22.getSlots().get(12).getItem() : ItemStack.EMPTY)
								.getItem() == RoboticaModItems.CENTRAL_PROCESSING_UNIT.get()
						&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu24 ? _menu24.getSlots().get(13).getItem() : ItemStack.EMPTY).getItem() == Blocks.COAL_BLOCK.asItem()) {
					RemoveItemsFromCraftingGridProcedure.execute(entity);
					if (entity instanceof Player _player && _player.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu) {
						ItemStack _setstack = new ItemStack(RoboticaModItems.BOGGIE_MK_1_EGG.get()).copy();
						_setstack.setCount(1);
						_menu.getSlots().get(20).set(_setstack);
						_player.containerMenu.broadcastChanges();
					}
				}
			}
		}
	}
}