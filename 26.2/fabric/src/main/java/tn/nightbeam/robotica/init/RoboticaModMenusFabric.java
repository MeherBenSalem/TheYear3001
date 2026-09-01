package tn.nightbeam.robotica.init;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MenuType;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.world.inventory.CraftingWorkStationGUIMenu;

public final class RoboticaModMenusFabric {
	private RoboticaModMenusFabric() {
	}

	public static void register() {
		MenuType<CraftingWorkStationGUIMenu> craftingWorkStationGui = new ExtendedMenuType<>(
				(syncId, inventory, data) -> new CraftingWorkStationGUIMenu(syncId, inventory, data),
				BlockPos.STREAM_CODEC);
		Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(RoboticaMod.MOD_ID, "crafting_work_station_gui"), craftingWorkStationGui);
		RoboticaModMenus.CRAFTING_WORK_STATION_GUI = () -> craftingWorkStationGui;
	}
}
