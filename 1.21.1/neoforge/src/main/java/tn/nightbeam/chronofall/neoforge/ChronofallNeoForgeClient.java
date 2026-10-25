package tn.nightbeam.chronofall.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;
import tn.nightbeam.chronofall.Chronofall;
import tn.nightbeam.chronofall.Constants;
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

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public final class ChronofallNeoForgeClient {
    public ChronofallNeoForgeClient(IEventBus modBus) {
        ChronofallClient.init();
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
            for (PlayerSkin.Model skin : event.getSkins()) {
                if (event.getSkin(skin) instanceof PlayerRenderer renderer) {
                    renderer.addLayer(new JetpackLayer(renderer, event.getEntityModels()));
                }
            }
        });
        modBus.addListener((RegisterMenuScreensEvent event) -> {
            event.register(ModContent.FABRICATOR_MENU.get(), FabricatorScreen::new);
            event.register(ModContent.FLUX_CHARGER_MENU.get(), FluxChargerScreen::new);
        });
        modBus.addListener((RegisterParticleProvidersEvent event) -> {
            event.registerSpriteSet(ModContent.FLUX_PARTICLE.get(), sprites -> new GlowParticle.Provider(sprites, GlowParticle.Kind.FLUX));
            event.registerSpriteSet(ModContent.RIFT_PARTICLE.get(), sprites -> new GlowParticle.Provider(sprites, GlowParticle.Kind.RIFT));
            event.registerSpriteSet(ModContent.LASER_PARTICLE.get(), sprites -> new GlowParticle.Provider(sprites, GlowParticle.Kind.LASER));
            event.registerSpriteSet(ModContent.THRUSTER_PARTICLE.get(), sprites -> new GlowParticle.Provider(sprites, GlowParticle.Kind.THRUSTER));
        });
        modBus.addListener((RegisterGuiLayersEvent event) -> event.registerAboveAll(Chronofall.id("flux_hud"), (graphics, delta) -> FluxHud.render(graphics)));
        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> ChronofallClient.tick(Minecraft.getInstance()));
    }
}
