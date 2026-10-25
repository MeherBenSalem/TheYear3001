package tn.nightbeam.chronofall.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import tn.nightbeam.chronofall.client.model.DroneModel;
import tn.nightbeam.chronofall.client.model.ModLayers;
import tn.nightbeam.chronofall.entity.DroneEntity;

public class DroneRenderer extends MobRenderer<DroneEntity, DroneModel> {
    public DroneRenderer(EntityRendererProvider.Context context) {
        super(context, new DroneModel(context.bakeLayer(ModLayers.DRONE)), 0.3F);
        addLayer(new GlowLayer<>(this, Textures.glow("drone"), drone -> !drone.poweredDown()));
    }

    @Override
    public ResourceLocation getTextureLocation(DroneEntity drone) {
        return Textures.entity("drone");
    }
}
