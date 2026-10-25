package tn.nightbeam.chronofall.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import tn.nightbeam.chronofall.client.model.ModLayers;
import tn.nightbeam.chronofall.client.model.SentinelModel;
import tn.nightbeam.chronofall.entity.SentinelEntity;

public class SentinelRenderer extends MobRenderer<SentinelEntity, RobotRenderState, SentinelModel> {
    public SentinelRenderer(EntityRendererProvider.Context context) {
        super(context, new SentinelModel(context.bakeLayer(ModLayers.SENTINEL)), 0.6F);
        addLayer(new GlowLayer<>(this, Textures.glow("sentinel")));
    }

    @Override
    public RobotRenderState createRenderState() {
        return new RobotRenderState();
    }

    @Override
    public void extractRenderState(SentinelEntity sentinel, RobotRenderState state, float partialTick) {
        super.extractRenderState(sentinel, state, partialTick);
        state.aggressive = sentinel.isAggressive();
        state.lit = !sentinel.isDeadOrDying();
    }

    @Override
    public Identifier getTextureLocation(RobotRenderState state) {
        return Textures.entity("sentinel");
    }
}
