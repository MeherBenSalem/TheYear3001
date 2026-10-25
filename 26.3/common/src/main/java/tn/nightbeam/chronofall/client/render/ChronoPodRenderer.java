package tn.nightbeam.chronofall.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Unit;
import tn.nightbeam.chronofall.client.model.ModLayers;
import tn.nightbeam.chronofall.client.model.PartModel;
import tn.nightbeam.chronofall.entity.ChronoPodEntity;

/** The falling pod: full-bright (it is burning), spinning, with its rift seams glowing. */
public class ChronoPodRenderer extends EntityRenderer<ChronoPodEntity, PartRenderState> {
    private final PartModel model;

    public ChronoPodRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new PartModel(context.bakeLayer(ModLayers.CHRONO_POD));
        shadowRadius = 0.6F;
    }

    @Override
    public PartRenderState createRenderState() {
        return new PartRenderState();
    }

    @Override
    public void extractRenderState(ChronoPodEntity pod, PartRenderState state, float partialTick) {
        super.extractRenderState(pod, state, partialTick);
        state.yRot = pod.spin(partialTick);
    }

    @Override
    public void submit(PartRenderState state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        pose.pushPose();
        pose.rotateDegrees(Axis.YP, state.yRot);
        pose.scale(-1.0F, -1.0F, 1.0F);
        pose.translate(0.0F, -1.501F, 0.0F);
        collector.submitModel(model, Unit.INSTANCE, pose, RenderTypes.entityCutout(Textures.entity("chrono_pod")), 0xF000F0,
                OverlayTexture.NO_OVERLAY, state.outlineColor);
        collector.order(1).submitModel(model, Unit.INSTANCE, pose, RenderTypes.eyes(Textures.glow("chrono_pod")), 0xF000F0, OverlayTexture.NO_OVERLAY,
                state.outlineColor);
        pose.popPose();
        super.submit(state, pose, collector, camera);
    }
}
