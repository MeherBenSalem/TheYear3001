package tn.nightbeam.chronofall.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import tn.nightbeam.chronofall.client.model.ModLayers;
import tn.nightbeam.chronofall.client.model.PartModel;
import tn.nightbeam.chronofall.entity.SkiffEntity;

/** The skiff hull, turned to its heading and banked into turns, with its glow strips drawn full-bright. */
public class SkiffRenderer extends EntityRenderer<SkiffEntity> {
    private final PartModel model;

    public SkiffRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new PartModel(context.bakeLayer(ModLayers.SKIFF));
        shadowRadius = 0.9F;
    }

    @Override
    public void render(SkiffEntity skiff, float yaw, float partialTick, PoseStack pose, MultiBufferSource buffers, int light) {
        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(180.0F - Mth.rotLerp(partialTick, skiff.yRotO, skiff.getYRot())));
        pose.translate(0, 0.45, 0);
        pose.mulPose(Axis.ZP.rotationDegrees(skiff.bank(partialTick)));
        pose.translate(0, -0.45, 0);
        pose.scale(-1.0F, -1.0F, 1.0F);
        pose.translate(0.0F, -1.501F, 0.0F);
        model.renderToBuffer(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(Textures.entity("skiff"))), light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (skiff.flux() > 0) {
            model.renderToBuffer(pose, buffers.getBuffer(RenderType.eyes(Textures.glow("skiff"))), 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        pose.popPose();
        super.render(skiff, yaw, partialTick, pose, buffers, light);
    }

    @Override
    public ResourceLocation getTextureLocation(SkiffEntity skiff) {
        return Textures.entity("skiff");
    }
}
