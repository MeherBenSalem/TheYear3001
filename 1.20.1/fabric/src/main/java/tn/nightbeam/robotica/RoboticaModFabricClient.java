package tn.nightbeam.robotica;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import tn.nightbeam.robotica.client.gui.CraftingWorkStationGUIScreen;
import tn.nightbeam.robotica.client.model.Modelboogie;
import tn.nightbeam.robotica.client.model.Modelmech;
import tn.nightbeam.robotica.client.model.Modelsentinel;
import tn.nightbeam.robotica.client.model.Modelship;
import tn.nightbeam.robotica.client.renderer.BoggieRenderer;
import tn.nightbeam.robotica.client.renderer.MechRenderer;
import tn.nightbeam.robotica.client.renderer.SentinelRenderer;
import tn.nightbeam.robotica.client.renderer.SpaceShipRenderer;
import tn.nightbeam.robotica.init.RoboticaModEntities;
import tn.nightbeam.robotica.init.RoboticaModFabricKeyMappings;
import tn.nightbeam.robotica.init.RoboticaModFluids;
import tn.nightbeam.robotica.world.inventory.CraftingWorkStationGUIMenu;
import tn.nightbeam.robotica.init.RoboticaModMenus;

public class RoboticaModFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(RoboticaModEntities.BOGGIE.get(), BoggieRenderer::new);
		EntityRendererRegistry.register(RoboticaModEntities.SENTINEL.get(), SentinelRenderer::new);
		EntityRendererRegistry.register(RoboticaModEntities.MECH.get(), MechRenderer::new);
		EntityRendererRegistry.register(RoboticaModEntities.SPACE_SHIP.get(), SpaceShipRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(Modelboogie.LAYER_LOCATION, Modelboogie::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(Modelmech.LAYER_LOCATION, Modelmech::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(Modelsentinel.LAYER_LOCATION, Modelsentinel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(Modelship.LAYER_LOCATION, Modelship::createBodyLayer);

		@SuppressWarnings("unchecked")
		MenuType<CraftingWorkStationGUIMenu> craftingMenu = (MenuType<CraftingWorkStationGUIMenu>) RoboticaModMenus.CRAFTING_WORK_STATION_GUI.get();
		MenuScreens.register(craftingMenu, CraftingWorkStationGUIScreen::new);
		RoboticaModFabricKeyMappings.register();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.screen == null) {
				RoboticaModFabricKeyMappings.FLY_CAPSULE_GO_UP.consumeClick();
				RoboticaModFabricKeyMappings.FLY_CAPSULE_GO_DOWN.consumeClick();
				RoboticaModFabricKeyMappings.FLY_CAPSULE_GO_FORWARD.consumeClick();
			}
		});

		ResourceLocation still = new ResourceLocation(RoboticaMod.MOD_ID, "block/oil_still");
		ResourceLocation flowing = new ResourceLocation(RoboticaMod.MOD_ID, "block/oil_flow");
		FluidRenderHandlerRegistry.INSTANCE.register(RoboticaModFluids.OIL.get(), RoboticaModFluids.FLOWING_OIL.get(),
				new SimpleFluidRenderHandler(still, flowing));
		BlockRenderLayerMap.INSTANCE.putFluids(RenderType.translucent(), RoboticaModFluids.OIL.get(), RoboticaModFluids.FLOWING_OIL.get());
	}
}
