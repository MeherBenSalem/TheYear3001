package tn.nightbeam.robotica;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import tn.nightbeam.robotica.client.gui.CraftingWorkStationGUIScreen;
import tn.nightbeam.robotica.client.renderer.BoggieRenderer;
import tn.nightbeam.robotica.client.renderer.MechRenderer;
import tn.nightbeam.robotica.client.renderer.SentinelRenderer;
import tn.nightbeam.robotica.client.renderer.SpaceShipRenderer;
import tn.nightbeam.robotica.init.RoboticaModEntities;
import tn.nightbeam.robotica.init.RoboticaModFabricKeyMappings;
import tn.nightbeam.robotica.init.RoboticaModMenus;
import tn.nightbeam.robotica.world.inventory.CraftingWorkStationGUIMenu;

public class RoboticaModFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(RoboticaModEntities.BOGGIE.get(), BoggieRenderer::new);
		EntityRendererRegistry.register(RoboticaModEntities.SENTINEL.get(), SentinelRenderer::new);
		EntityRendererRegistry.register(RoboticaModEntities.MECH.get(), MechRenderer::new);
		EntityRendererRegistry.register(RoboticaModEntities.SPACE_SHIP.get(), SpaceShipRenderer::new);

		@SuppressWarnings("unchecked")
		MenuType<CraftingWorkStationGUIMenu> craftingMenu = (MenuType<CraftingWorkStationGUIMenu>) RoboticaModMenus.CRAFTING_WORK_STATION_GUI.get();
		MenuScreens.register(craftingMenu, CraftingWorkStationGUIScreen::new);
		RoboticaModFabricKeyMappings.register();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.gui.screen() == null) {
				RoboticaModFabricKeyMappings.FLY_CAPSULE_GO_UP.consumeClick();
				RoboticaModFabricKeyMappings.FLY_CAPSULE_GO_DOWN.consumeClick();
				RoboticaModFabricKeyMappings.FLY_CAPSULE_GO_FORWARD.consumeClick();
			}
		});
	}
}
