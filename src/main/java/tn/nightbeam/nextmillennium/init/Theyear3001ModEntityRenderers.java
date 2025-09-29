
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.nextmillennium.init;

import tn.nightbeam.nextmillennium.client.renderer.SentinelMK1Renderer;
import tn.nightbeam.nextmillennium.client.renderer.MechMK1Renderer;
import tn.nightbeam.nextmillennium.client.renderer.FlyingCapsuleMechRenderer;
import tn.nightbeam.nextmillennium.client.renderer.BoggieMK1Renderer;
import tn.nightbeam.nextmillennium.client.renderer.B2050Renderer;
import tn.nightbeam.nextmillennium.client.renderer.B1990Renderer;
import tn.nightbeam.nextmillennium.client.renderer.B1928Renderer;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Theyear3001ModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(Theyear3001ModEntities.BOGGIE_MK_1.get(), BoggieMK1Renderer::new);
		event.registerEntityRenderer(Theyear3001ModEntities.MECH_MK_1.get(), MechMK1Renderer::new);
		event.registerEntityRenderer(Theyear3001ModEntities.B_1928.get(), B1928Renderer::new);
		event.registerEntityRenderer(Theyear3001ModEntities.B_1990.get(), B1990Renderer::new);
		event.registerEntityRenderer(Theyear3001ModEntities.B_2050.get(), B2050Renderer::new);
		event.registerEntityRenderer(Theyear3001ModEntities.SENTINEL_MK_1.get(), SentinelMK1Renderer::new);
		event.registerEntityRenderer(Theyear3001ModEntities.FLYING_CAPSULE_MECH.get(), FlyingCapsuleMechRenderer::new);
	}
}
