package tn.nightbeam.robotica.init;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.world.inventory.CraftingWorkStationGUIMenu;

public class RoboticaModMenusFabric {
	public static void register() {
		MenuType<CraftingWorkStationGUIMenu> craftingWorkStationGui = new ExtendedScreenHandlerType<>(CraftingWorkStationGUIMenu::new);
		Registry.register(BuiltInRegistries.MENU, new ResourceLocation(RoboticaMod.MOD_ID, "crafting_work_station_gui"),
				craftingWorkStationGui);
		RoboticaModMenus.CRAFTING_WORK_STATION_GUI = () -> craftingWorkStationGui;
	}
}
