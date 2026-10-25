package tn.nightbeam.chronofall.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import tn.nightbeam.chronofall.client.ChronofallClient;
import tn.nightbeam.chronofall.client.FluxHud;
import tn.nightbeam.chronofall.client.GlowParticle;
import tn.nightbeam.chronofall.client.model.ModLayers;
import tn.nightbeam.chronofall.client.render.ChronoPodRenderer;
import tn.nightbeam.chronofall.client.render.DroneRenderer;
import tn.nightbeam.chronofall.client.render.FabricatorRenderer;
import tn.nightbeam.chronofall.client.render.JetpackLayer;
import tn.nightbeam.chronofall.client.render.MechRenderer;
import tn.nightbeam.chronofall.client.render.SentinelRenderer;
import tn.nightbeam.chronofall.client.render.SkiffRenderer;
import tn.nightbeam.chronofall.client.screen.FabricatorScreen;
import tn.nightbeam.chronofall.client.screen.FluxChargerScreen;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.network.ControlPayload;

/** Client-only setup, called from {@link ChronofallForge} on the client. */
final class ChronofallForgeClient {
    private ChronofallForgeClient() {
    }

    static void init(IEventBus modBus) {
        ChronofallClient.init();
        ChronofallForge.controlSender = flags -> ChronofallForge.CHANNEL.sendToServer(new ControlPayload(flags));
        modBus.addListener((FMLClientSetupEvent event) -> event.enqueueWork(() -> {
            MenuScreens.register(ModContent.FABRICATOR_MENU.get(), FabricatorScreen::new);
            MenuScreens.register(ModContent.FLUX_CHARGER_MENU.get(), FluxChargerScreen::new);
        }));
        modBus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event) ->
                ModLayers.ALL.forEach((layer, definition) -> event.registerLayerDefinition(layer, definition)));
        modBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> {
            event.registerEntityRenderer(ModContent.SENTINEL.get(), SentinelRenderer::new);
            event.registerEntityRenderer(ModContent.DRONE.get(), DroneRenderer::new);
            event.registerEntityRenderer(ModContent.MECH.get(), MechRenderer::new);
            event.registerEntityRenderer(ModContent.SKIFF.get(), SkiffRenderer::new);
            event.registerEntityRenderer(ModContent.CHRONO_POD.get(), ChronoPodRenderer::new);
            event.registerEntityRenderer(ModContent.LASER_BOLT.get(), NoopRenderer::new);
            event.registerBlockEntityRenderer(ModContent.FABRICATOR_ENTITY.get(), FabricatorRenderer::new);
        });
        modBus.addListener((EntityRenderersEvent.AddLayers event) -> {
            for (String skin : event.getSkins()) {
                if (event.getSkin(skin) instanceof PlayerRenderer renderer) {
                    renderer.addLayer(new JetpackLayer(renderer, event.getEntityModels()));
                }
            }
        });
        modBus.addListener((RegisterParticleProvidersEvent event) -> {
            event.registerSpriteSet(ModContent.FLUX_PARTICLE.get(), sprites -> new GlowParticle.Provider(sprites, GlowParticle.Kind.FLUX));
            event.registerSpriteSet(ModContent.RIFT_PARTICLE.get(), sprites -> new GlowParticle.Provider(sprites, GlowParticle.Kind.RIFT));
            event.registerSpriteSet(ModContent.LASER_PARTICLE.get(), sprites -> new GlowParticle.Provider(sprites, GlowParticle.Kind.LASER));
            event.registerSpriteSet(ModContent.THRUSTER_PARTICLE.get(), sprites -> new GlowParticle.Provider(sprites, GlowParticle.Kind.THRUSTER));
        });
        modBus.addListener((RegisterGuiOverlaysEvent event) ->
                event.registerAboveAll("flux_hud", (gui, graphics, partialTick, width, height) -> FluxHud.render(graphics)));
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (event.phase == TickEvent.Phase.END) {
                ChronofallClient.tick(Minecraft.getInstance());
            }
        });
    }
}
