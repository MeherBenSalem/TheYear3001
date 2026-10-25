package tn.nightbeam.chronofall.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import tn.nightbeam.chronofall.client.model.ModLayers;
import tn.nightbeam.chronofall.client.model.PartModel;
import tn.nightbeam.chronofall.entity.ChronoPodEntity;

/** The falling pod: full-bright (it is burning), spinning, with its rift seams glowing. */
public class ChronoPodRenderer extends EntityRenderer<ChronoPodEntity> {
    private final PartModel model;

    public ChronoPodRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new PartModel(context.bakeLayer(ModLayers.CHRONO_POD));
        shadowRadius = 0.6F;
    }

    @Override
    public void render(ChronoPodEntity pod, float yaw, float partialTick, PoseStack pose, MultiBufferSource buffers, int light) {
        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(pod.spin(partialTick)));
        pose.scale(-1.0F, -1.0F, 1.0F);
        pose.translate(0.0F, -1.501F, 0.0F);
        model.renderToBuffer(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(Textures.entity("chrono_pod"))), 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        model.renderToBuffer(pose, buffers.getBuffer(RenderType.eyes(Textures.glow("chrono_pod"))), 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pose.popPose();
        super.render(pod, yaw, partialTick, pose, buffers, light);
    }

    @Override
    public ResourceLocation getTextureLocation(ChronoPodEntity pod) {
        return Textures.entity("chrono_pod");
    }
}
