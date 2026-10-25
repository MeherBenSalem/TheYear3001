package tn.nightbeam.chronofall.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import tn.nightbeam.chronofall.client.model.DroneModel;
import tn.nightbeam.chronofall.client.model.ModLayers;
import tn.nightbeam.chronofall.entity.DroneEntity;

public class DroneRenderer extends MobRenderer<DroneEntity, RobotRenderState, DroneModel> {
    public DroneRenderer(EntityRendererProvider.Context context) {
        super(context, new DroneModel(context.bakeLayer(ModLayers.DRONE)), 0.3F);
        addLayer(new GlowLayer<>(this, Textures.glow("drone")));
    }

    @Override
    public RobotRenderState createRenderState() {
        return new RobotRenderState();
    }

    @Override
    public void extractRenderState(DroneEntity drone, RobotRenderState state, float partialTick) {
        super.extractRenderState(drone, state, partialTick);
        state.poweredDown = drone.poweredDown();
        state.lit = !state.poweredDown;
    }

    @Override
    public Identifier getTextureLocation(RobotRenderState state) {
        return Textures.entity("drone");
    }
}
