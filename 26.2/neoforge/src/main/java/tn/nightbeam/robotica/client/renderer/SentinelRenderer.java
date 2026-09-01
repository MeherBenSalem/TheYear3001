package tn.nightbeam.robotica.client.renderer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import tn.nightbeam.robotica.entity.SentinelEntity;

public class SentinelRenderer extends MobRenderer<SentinelEntity, HumanoidRenderState, HumanoidModel<HumanoidRenderState>> {
	public SentinelRenderer(EntityRendererProvider.Context context) {
		super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
	}

	@Override
	public HumanoidRenderState createRenderState() {
		return new HumanoidRenderState();
	}

	@Override
	public Identifier getTextureLocation(HumanoidRenderState state) {
		return Identifier.fromNamespaceAndPath("robotica", "textures/entities/robot_1.png");
	}
}
