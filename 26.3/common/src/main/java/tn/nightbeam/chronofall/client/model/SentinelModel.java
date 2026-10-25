package tn.nightbeam.chronofall.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import tn.nightbeam.chronofall.client.render.RobotRenderState;

public class SentinelModel extends EntityModel<RobotRenderState> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public SentinelModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
        head = root.getChild("head");
        body = root.getChild("body");
        rightArm = root.getChild("right_arm");
        leftArm = root.getChild("left_arm");
        rightLeg = root.getChild("right_leg");
        leftLeg = root.getChild("left_leg");
    }

    @Override
    public void setupAnim(RobotRenderState state) {
        super.setupAnim(state);
        float age = state.ageInTicks;
        head.yRot = state.yRot * Mth.DEG_TO_RAD;
        head.xRot = state.xRot * Mth.DEG_TO_RAD;
        float swing = Mth.cos(state.walkAnimationPos * 0.55F) * 1.1F * state.walkAnimationSpeed;
        rightLeg.xRot = swing;
        leftLeg.xRot = -swing;
        leftArm.xRot = swing * 0.7F;
        leftArm.zRot = -0.08F - Mth.sin(age * 0.07F) * 0.04F;
        body.y = -3.0F + Mth.sin(age * 0.1F) * 0.2F;
        head.y = body.y;
        if (state.aggressive) {
            rightArm.xRot = -Mth.HALF_PI + head.xRot;
            rightArm.yRot = head.yRot * 0.8F;
            rightArm.zRot = 0;
        } else {
            rightArm.xRot = -swing * 0.7F;
            rightArm.yRot = 0;
            rightArm.zRot = 0.08F + Mth.sin(age * 0.07F) * 0.04F;
        }
    }
}
