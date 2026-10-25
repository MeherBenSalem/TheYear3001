package tn.nightbeam.chronofall.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import tn.nightbeam.chronofall.entity.SentinelEntity;

public class SentinelModel extends HierarchicalModel<SentinelEntity> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public SentinelModel(ModelPart root) {
        this.root = root;
        head = root.getChild("head");
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
    public void setupAnim(SentinelEntity sentinel, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        head.xRot = headPitch * Mth.DEG_TO_RAD;
        float swing = Mth.cos(limbSwing * 0.55F) * 1.1F * limbSwingAmount;
        rightLeg.xRot = swing;
        leftLeg.xRot = -swing;
        leftArm.xRot = swing * 0.7F;
        leftArm.zRot = -0.08F - Mth.sin(ageInTicks * 0.07F) * 0.04F;
        body.y = -3.0F + Mth.sin(ageInTicks * 0.1F) * 0.2F;
        head.y = body.y;
        if (sentinel.isAggressive()) {
            rightArm.xRot = -Mth.HALF_PI + head.xRot;
            rightArm.yRot = head.yRot * 0.8F;
            rightArm.zRot = 0;
        } else {
            rightArm.xRot = -swing * 0.7F;
            rightArm.yRot = 0;
            rightArm.zRot = 0.08F + Mth.sin(ageInTicks * 0.07F) * 0.04F;
        }
    }
}
