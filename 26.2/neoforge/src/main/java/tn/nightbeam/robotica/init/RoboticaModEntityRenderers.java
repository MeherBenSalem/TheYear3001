package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.client.renderer.SpaceShipRenderer;
import tn.nightbeam.robotica.client.renderer.SentinelRenderer;
import tn.nightbeam.robotica.client.renderer.MechRenderer;
import tn.nightbeam.robotica.client.renderer.BoggieRenderer;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = RoboticaMod.MOD_ID, value = Dist.CLIENT)
public class RoboticaModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(RoboticaModEntitiesNeoForge.BOGGIE.get(), BoggieRenderer::new);
		event.registerEntityRenderer(RoboticaModEntitiesNeoForge.SENTINEL.get(), SentinelRenderer::new);
		event.registerEntityRenderer(RoboticaModEntitiesNeoForge.MECH.get(), MechRenderer::new);
		event.registerEntityRenderer(RoboticaModEntitiesNeoForge.SPACE_SHIP.get(), SpaceShipRenderer::new);
	}
}
