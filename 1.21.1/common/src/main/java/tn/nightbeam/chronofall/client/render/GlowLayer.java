package tn.nightbeam.chronofall.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Predicate;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

/** Draws the model again with its glow texture, full-bright and additive, so lights and visors shine in the dark. */
public class GlowLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final RenderType type;
    private final Predicate<T> lit;

    public GlowLayer(RenderLayerParent<T, M> parent, ResourceLocation texture, Predicate<T> lit) {
        super(parent);
        this.type = RenderType.eyes(texture);
        this.lit = lit;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffers, int light, T entity, float limbSwing, float limbSwingAmount, float partialTick,
                       float ageInTicks, float netHeadYaw, float headPitch) {
        if (lit.test(entity)) {
            getParentModel().renderToBuffer(poseStack, buffers.getBuffer(type), 0xF000F0, OverlayTexture.NO_OVERLAY);
        }
    }
}
