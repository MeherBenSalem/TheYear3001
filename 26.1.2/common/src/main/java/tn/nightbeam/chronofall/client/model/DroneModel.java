package tn.nightbeam.chronofall.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import tn.nightbeam.chronofall.client.render.RobotRenderState;

public class DroneModel extends EntityModel<RobotRenderState> {
    private final ModelPart body;
    private final ModelPart[] rotors = new ModelPart[4];

    public DroneModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
        this.body = root.getChild("body");
        for (int i = 0; i < 4; i++) {
            rotors[i] = body.getChild("arm" + i).getChild("rotor" + i);
        }
    }

    @Override
    public void setupAnim(RobotRenderState state) {
        super.setupAnim(state);
        boolean on = !state.poweredDown;
        float age = state.ageInTicks;
        for (int i = 0; i < 4; i++) {
            rotors[i].yRot = on ? age * 1.6F * (i % 2 == 0 ? 1 : -1) : i * 0.4F;
        }
        body.xRot = on ? Mth.clamp(state.walkAnimationSpeed * 0.8F, 0, 0.35F) + state.xRot * Mth.DEG_TO_RAD * 0.3F : 0.25F;
        body.yRot = state.yRot * Mth.DEG_TO_RAD * 0.5F;
        body.y = 18.0F + (on ? Mth.sin(age * 0.18F) * 0.6F : 2.0F);
        body.zRot = on ? Mth.sin(age * 0.09F) * 0.05F : 0.3F;
    }
}
