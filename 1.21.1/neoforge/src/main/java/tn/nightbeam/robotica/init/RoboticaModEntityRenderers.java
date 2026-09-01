package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.RoboticaMod;

import tn.nightbeam.robotica.client.renderer.SpaceShipRenderer;
import tn.nightbeam.robotica.client.renderer.SentinelRenderer;
import tn.nightbeam.robotica.client.renderer.MechRenderer;
import tn.nightbeam.robotica.client.renderer.BoggieRenderer;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(modid = RoboticaMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RoboticaModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(RoboticaModEntitiesForge.BOGGIE.get(), BoggieRenderer::new);
		event.registerEntityRenderer(RoboticaModEntitiesForge.SENTINEL.get(), SentinelRenderer::new);
		event.registerEntityRenderer(RoboticaModEntitiesForge.MECH.get(), MechRenderer::new);
		event.registerEntityRenderer(RoboticaModEntitiesForge.SPACE_SHIP.get(), SpaceShipRenderer::new);
	}
}