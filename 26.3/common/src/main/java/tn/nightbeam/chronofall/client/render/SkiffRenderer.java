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
import tn.nightbeam.chronofall.entity.SkiffEntity;

/** The skiff hull, turned to its heading and banked into turns, with its glow strips drawn full-bright. */
public class SkiffRenderer extends EntityRenderer<SkiffEntity, PartRenderState> {
    private final PartModel model;

    public SkiffRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new PartModel(context.bakeLayer(ModLayers.SKIFF));
        shadowRadius = 0.9F;
    }

    @Override
    public PartRenderState createRenderState() {
        return new PartRenderState();
    }

    @Override
    public void extractRenderState(SkiffEntity skiff, PartRenderState state, float partialTick) {
        super.extractRenderState(skiff, state, partialTick);
        state.yRot = skiff.getYRot(partialTick);
        state.roll = skiff.bank(partialTick);
        state.lit = skiff.flux() > 0;
    }

    @Override
    public void submit(PartRenderState state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        pose.pushPose();
        pose.rotateDegrees(Axis.YP, 180.0F - state.yRot);
        pose.translate(0, 0.45, 0);
        pose.rotateDegrees(Axis.ZP, state.roll);
        pose.translate(0, -0.45, 0);
        pose.scale(-1.0F, -1.0F, 1.0F);
        pose.translate(0.0F, -1.501F, 0.0F);
        collector.submitModel(model, Unit.INSTANCE, pose, RenderTypes.entityCutout(Textures.entity("skiff")), state.lightCoords,
                OverlayTexture.NO_OVERLAY, state.outlineColor);
        if (state.lit) {
            collector.order(1).submitModel(model, Unit.INSTANCE, pose, RenderTypes.eyes(Textures.glow("skiff")), 0xF000F0, OverlayTexture.NO_OVERLAY,
                    state.outlineColor);
        }
        pose.popPose();
        super.submit(state, pose, collector, camera);
    }
}
