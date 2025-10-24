
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.world.inventory.CraftingWorkStationGUIMenu;
import tn.nightbeam.robotica.RoboticaMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

public class RoboticaModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, RoboticaMod.MODID);
	public static final RegistryObject<MenuType<CraftingWorkStationGUIMenu>> CRAFTING_WORK_STATION_GUI = REGISTRY.register("crafting_work_station_gui", () -> IForgeMenuType.create(CraftingWorkStationGUIMenu::new));
}
