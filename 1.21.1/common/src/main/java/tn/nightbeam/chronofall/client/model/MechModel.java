package tn.nightbeam.chronofall.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import tn.nightbeam.chronofall.entity.MechEntity;

public class MechModel extends HierarchicalModel<MechEntity> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public MechModel(ModelPart root) {
        this.root = root;
        body = root.getChild("body");
        rightArm = root.getChild("right_arm");
        leftArm = root.getChild("left_arm");
        rightLeg = root.getChild("right_leg");
        leftLeg = root.getChild("left_leg");
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(MechEntity mech, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float swing = Mth.cos(limbSwing * 0.4F) * 0.8F * limbSwingAmount;
        rightLeg.xRot = swing;
        leftLeg.xRot = -swing;
        float step = Math.abs(Mth.sin(limbSwing * 0.4F)) * limbSwingAmount;
        body.y = 4.0F - step * 1.2F + Mth.sin(ageInTicks * 0.08F) * 0.15F;
        body.zRot = Mth.sin(limbSwing * 0.4F) * 0.04F * limbSwingAmount;
        rightArm.y = body.y - 12.0F;
        leftArm.y = body.y - 12.0F;
        int slam = mech.slamTicks();
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
