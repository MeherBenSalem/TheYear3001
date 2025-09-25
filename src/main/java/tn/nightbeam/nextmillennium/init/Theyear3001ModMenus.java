
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.nextmillennium.init;

import tn.nightbeam.nextmillennium.world.inventory.CraftingWorkStationGUIMenu;
import tn.nightbeam.nextmillennium.Theyear3001Mod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

public class Theyear3001ModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Theyear3001Mod.MODID);
	public static final RegistryObject<MenuType<CraftingWorkStationGUIMenu>> CRAFTING_WORK_STATION_GUI = REGISTRY.register("crafting_work_station_gui", () -> IForgeMenuType.create(CraftingWorkStationGUIMenu::new));
}
