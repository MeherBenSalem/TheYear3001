package tn.nightbeam.chronofall.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

/** Draws the model again with its glow texture, full-bright and additive, so lights and visors shine in the dark. */
public class GlowLayer<M extends EntityModel<RobotRenderState>> extends RenderLayer<RobotRenderState, M> {
    private final RenderType type;

    public GlowLayer(RenderLayerParent<RobotRenderState, M> parent, Identifier texture) {
        super(parent);
        this.type = RenderTypes.eyes(texture);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, RobotRenderState state, float yRot, float xRot) {
        if (state.lit) {
            collector.order(1).submitModel(getParentModel(), state, poseStack, type, 0xF000F0, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        }
    }
}
