package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.world.inventory.CraftingWorkStationGUIMenu;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import net.minecraft.world.inventory.MenuType;

import tn.nightbeam.robotica.RoboticaMod;

public class RoboticaModMenusNeoForge {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.MENU, RoboticaMod.MOD_ID);
	public static final DeferredHolder<MenuType<?>, MenuType<CraftingWorkStationGUIMenu>> CRAFTING_WORK_STATION_GUI = REGISTRY
			.register("crafting_work_station_gui", () -> IMenuTypeExtension.create(CraftingWorkStationGUIMenu::new));

	static {
		RoboticaModMenus.CRAFTING_WORK_STATION_GUI = () -> CRAFTING_WORK_STATION_GUI.get();
	}
}
