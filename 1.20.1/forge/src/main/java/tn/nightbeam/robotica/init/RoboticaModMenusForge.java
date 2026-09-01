package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.world.inventory.CraftingWorkStationGUIMenu;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

import tn.nightbeam.robotica.RoboticaMod;

public class RoboticaModMenusForge {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, RoboticaMod.MOD_ID);
	public static final RegistryObject<MenuType<CraftingWorkStationGUIMenu>> CRAFTING_WORK_STATION_GUI = REGISTRY
			.register("crafting_work_station_gui", () -> IForgeMenuType.create(CraftingWorkStationGUIMenu::new));

	static {
		RoboticaModMenus.CRAFTING_WORK_STATION_GUI = () -> CRAFTING_WORK_STATION_GUI.get();
	}
}
