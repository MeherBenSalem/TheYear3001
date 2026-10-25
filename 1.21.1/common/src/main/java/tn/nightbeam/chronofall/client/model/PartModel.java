package tn.nightbeam.chronofall.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;

/** A model that is just its parts, for things that are not living entities (skiff, pod, jetpack). */
public class PartModel extends Model {
    private final ModelPart root;

    public PartModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
    }

    public ModelPart root() {
        return root;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int light, int overlay, int color) {
        root.render(poseStack, buffer, light, overlay, color);
    }
}
