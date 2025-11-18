package tn.nightbeam.robotica.procedures;

import tn.nightbeam.robotica.init.RoboticaModMenus;
import tn.nightbeam.robotica.init.RoboticaModItems;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class CheckCircleScrapProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu0 ? _menu0.getSlots().get(0).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.SCRAP.get()
				&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu2 ? _menu2.getSlots().get(1).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.SCRAP.get()
				&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu4 ? _menu4.getSlots().get(2).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.SCRAP.get()
				&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu6 ? _menu6.getSlots().get(3).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.SCRAP.get()
				&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu8 ? _menu8.getSlots().get(4).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.SCRAP.get()
				&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu10 ? _menu10.getSlots().get(5).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.SCRAP.get()
				&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu12 ? _menu12.getSlots().get(9).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.SCRAP.get()
				&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu14 ? _menu14.getSlots().get(10).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.SCRAP.get()
				&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu16 ? _menu16.getSlots().get(14).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.SCRAP.get()
				&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu18 ? _menu18.getSlots().get(15).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.SCRAP.get()
				&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu20 ? _menu20.getSlots().get(16).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.SCRAP.get()
				&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu22 ? _menu22.getSlots().get(17).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.SCRAP.get()
				&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu24 ? _menu24.getSlots().get(18).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.SCRAP.get()
				&& (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof RoboticaModMenus.MenuAccessor _menu26 ? _menu26.getSlots().get(19).getItem() : ItemStack.EMPTY).getItem() == RoboticaModItems.SCRAP.get()) {
			return true;
		}
		return false;
	}
}