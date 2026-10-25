package tn.nightbeam.chronofall.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import tn.nightbeam.chronofall.client.render.RobotRenderState;

public class MechModel extends EntityModel<RobotRenderState> {
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public MechModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
        body = root.getChild("body");
        rightArm = root.getChild("right_arm");
        leftArm = root.getChild("left_arm");
        rightLeg = root.getChild("right_leg");
        leftLeg = root.getChild("left_leg");
    }

    @Override
    public void setupAnim(RobotRenderState state) {
        super.setupAnim(state);
        float pos = state.walkAnimationPos;
        float speed = state.walkAnimationSpeed;
        float swing = Mth.cos(pos * 0.4F) * 0.8F * speed;
        rightLeg.xRot = swing;
        leftLeg.xRot = -swing;
        float step = Math.abs(Mth.sin(pos * 0.4F)) * speed;
        body.y = 4.0F - step * 1.2F + Mth.sin(state.ageInTicks * 0.08F) * 0.15F;
        body.zRot = Mth.sin(pos * 0.4F) * 0.04F * speed;
        rightArm.y = body.y - 12.0F;
        leftArm.y = body.y - 12.0F;
        int slam = state.slamTicks;
        if (slam > 0) {
            float raise = slam > 6 ? 1.0F : slam / 6.0F;
            rightArm.xRot = -2.6F * raise;
            leftArm.xRot = -2.6F * raise;
        } else {
            rightArm.xRot = -swing * 0.6F;
            leftArm.xRot = swing * 0.6F;
        }
        rightArm.zRot = 0.06F;
        leftArm.zRot = -0.06F;
    }
}
