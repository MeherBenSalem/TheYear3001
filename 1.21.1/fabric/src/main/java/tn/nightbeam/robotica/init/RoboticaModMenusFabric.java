package tn.nightbeam.robotica.init;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.world.inventory.CraftingWorkStationGUIMenu;

public class RoboticaModMenusFabric {
	public static void register() {
		MenuType<CraftingWorkStationGUIMenu> craftingWorkStationGui = new ExtendedScreenHandlerType<>(
				(syncId, inventory, data) -> new CraftingWorkStationGUIMenu(syncId, inventory,
						new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(data)),
				BlockPos.STREAM_CODEC);
		Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(RoboticaMod.MOD_ID, "crafting_work_station_gui"),
				craftingWorkStationGui);
		RoboticaModMenus.CRAFTING_WORK_STATION_GUI = () -> craftingWorkStationGui;
	}
}
