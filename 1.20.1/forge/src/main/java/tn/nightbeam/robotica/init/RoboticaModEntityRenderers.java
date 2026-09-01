package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.client.renderer.SpaceShipRenderer;
import tn.nightbeam.robotica.client.renderer.SentinelRenderer;
import tn.nightbeam.robotica.client.renderer.MechRenderer;
import tn.nightbeam.robotica.client.renderer.BoggieRenderer;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RoboticaModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(RoboticaModEntitiesForge.BOGGIE.get(), BoggieRenderer::new);
		event.registerEntityRenderer(RoboticaModEntitiesForge.SENTINEL.get(), SentinelRenderer::new);
		event.registerEntityRenderer(RoboticaModEntitiesForge.MECH.get(), MechRenderer::new);
		event.registerEntityRenderer(RoboticaModEntitiesForge.SPACE_SHIP.get(), SpaceShipRenderer::new);
	}
}