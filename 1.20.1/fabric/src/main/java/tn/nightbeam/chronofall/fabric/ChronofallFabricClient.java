package tn.nightbeam.chronofall.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
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

public final class ChronofallFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ChronofallClient.init();
        ChronofallFabric.controlSender = flags -> {
            if (ClientPlayNetworking.canSend(ControlPayload.ID)) {
                net.minecraft.network.FriendlyByteBuf buf = net.fabricmc.fabric.api.networking.v1.PacketByteBufs.create();
                new ControlPayload(flags).write(buf);
                ClientPlayNetworking.send(ControlPayload.ID, buf);
            }
        };
        ModLayers.ALL.forEach((layer, definition) -> EntityModelLayerRegistry.registerModelLayer(layer, definition::get));
        EntityRendererRegistry.register(ModContent.SENTINEL.get(), SentinelRenderer::new);
        EntityRendererRegistry.register(ModContent.DRONE.get(), DroneRenderer::new);
        EntityRendererRegistry.register(ModContent.MECH.get(), MechRenderer::new);
        EntityRendererRegistry.register(ModContent.SKIFF.get(), SkiffRenderer::new);
        EntityRendererRegistry.register(ModContent.CHRONO_POD.get(), ChronoPodRenderer::new);
        EntityRendererRegistry.register(ModContent.LASER_BOLT.get(), NoopRenderer::new);
        BlockEntityRenderers.register(ModContent.FABRICATOR_ENTITY.get(), FabricatorRenderer::new);
        MenuScreens.register(ModContent.FABRICATOR_MENU.get(), FabricatorScreen::new);
        MenuScreens.register(ModContent.FLUX_CHARGER_MENU.get(), FluxChargerScreen::new);
        ParticleFactoryRegistry particles = ParticleFactoryRegistry.getInstance();
        particles.register(ModContent.FLUX_PARTICLE.get(), sprites -> new GlowParticle.Provider(sprites, GlowParticle.Kind.FLUX));
        particles.register(ModContent.RIFT_PARTICLE.get(), sprites -> new GlowParticle.Provider(sprites, GlowParticle.Kind.RIFT));
        particles.register(ModContent.LASER_PARTICLE.get(), sprites -> new GlowParticle.Provider(sprites, GlowParticle.Kind.LASER));
        particles.register(ModContent.THRUSTER_PARTICLE.get(), sprites -> new GlowParticle.Provider(sprites, GlowParticle.Kind.THRUSTER));
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((type, renderer, helper, context) -> {
            if (renderer instanceof PlayerRenderer player) {
                helper.register(new JetpackLayer(player, context.getModelSet()));
            }
        });
        HudRenderCallback.EVENT.register((graphics, tick) -> FluxHud.render(graphics));
        ClientTickEvents.END_CLIENT_TICK.register(ChronofallClient::tick);
    }
}
