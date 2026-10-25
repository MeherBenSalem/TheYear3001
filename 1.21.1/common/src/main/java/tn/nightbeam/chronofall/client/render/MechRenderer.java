package tn.nightbeam.chronofall.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import tn.nightbeam.chronofall.client.model.MechModel;
import tn.nightbeam.chronofall.client.model.ModLayers;
import tn.nightbeam.chronofall.entity.MechEntity;

public class MechRenderer extends MobRenderer<MechEntity, MechModel> {
    public MechRenderer(EntityRendererProvider.Context context) {
        super(context, new MechModel(context.bakeLayer(ModLayers.MECH)), 1.2F);
        addLayer(new GlowLayer<>(this, Textures.glow("mech"), mech -> mech.flux() > 0));
    }

    @Override
    public ResourceLocation getTextureLocation(MechEntity mech) {
        return Textures.entity("mech");
    }
}
