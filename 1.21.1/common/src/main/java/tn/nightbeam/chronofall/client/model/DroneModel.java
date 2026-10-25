package tn.nightbeam.chronofall.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import tn.nightbeam.chronofall.entity.DroneEntity;

public class DroneModel extends HierarchicalModel<DroneEntity> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart[] rotors = new ModelPart[4];

    public DroneModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        for (int i = 0; i < 4; i++) {
            rotors[i] = body.getChild("arm" + i).getChild("rotor" + i);
        }
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(DroneEntity drone, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean on = !drone.poweredDown();
        for (int i = 0; i < 4; i++) {
            rotors[i].yRot = on ? ageInTicks * 1.6F * (i % 2 == 0 ? 1 : -1) : i * 0.4F;
        }
        body.xRot = on ? Mth.clamp(limbSwingAmount * 0.8F, 0, 0.35F) + headPitch * Mth.DEG_TO_RAD * 0.3F : 0.25F;
        body.yRot = netHeadYaw * Mth.DEG_TO_RAD * 0.5F;
        body.y = 18.0F + (on ? Mth.sin(ageInTicks * 0.18F) * 0.6F : 2.0F);
        body.zRot = on ? Mth.sin(ageInTicks * 0.09F) * 0.05F : 0.3F;
    }
}
