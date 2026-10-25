package tn.nightbeam.chronofall.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import tn.nightbeam.chronofall.client.model.ModLayers;
import tn.nightbeam.chronofall.client.model.SentinelModel;
import tn.nightbeam.chronofall.entity.SentinelEntity;

public class SentinelRenderer extends MobRenderer<SentinelEntity, SentinelModel> {
    public SentinelRenderer(EntityRendererProvider.Context context) {
        super(context, new SentinelModel(context.bakeLayer(ModLayers.SENTINEL)), 0.6F);
        addLayer(new GlowLayer<>(this, Textures.glow("sentinel"), sentinel -> !sentinel.isDeadOrDying()));
    }

    @Override
    public ResourceLocation getTextureLocation(SentinelEntity sentinel) {
        return Textures.entity("sentinel");
    }
}
