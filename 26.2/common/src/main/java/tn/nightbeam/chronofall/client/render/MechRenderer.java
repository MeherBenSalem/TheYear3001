package tn.nightbeam.chronofall.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import tn.nightbeam.chronofall.client.model.MechModel;
import tn.nightbeam.chronofall.client.model.ModLayers;
import tn.nightbeam.chronofall.entity.MechEntity;

public class MechRenderer extends MobRenderer<MechEntity, RobotRenderState, MechModel> {
    public MechRenderer(EntityRendererProvider.Context context) {
        super(context, new MechModel(context.bakeLayer(ModLayers.MECH)), 1.2F);
        addLayer(new GlowLayer<>(this, Textures.glow("mech")));
    }

    @Override
    public RobotRenderState createRenderState() {
        return new RobotRenderState();
    }

    @Override
    public void extractRenderState(MechEntity mech, RobotRenderState state, float partialTick) {
        super.extractRenderState(mech, state, partialTick);
        state.slamTicks = mech.slamTicks();
        state.lit = mech.flux() > 0;
    }

    @Override
    public Identifier getTextureLocation(RobotRenderState state) {
        return Textures.entity("mech");
    }
}
