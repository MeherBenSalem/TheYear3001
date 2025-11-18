package tn.nightbeam.robotica.client.renderer;

import tn.nightbeam.robotica.entity.SpaceShipEntity;
import tn.nightbeam.robotica.client.model.animations.shipAnimation;
import tn.nightbeam.robotica.client.model.Modelship;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HierarchicalModel;

public class SpaceShipRenderer extends MobRenderer<SpaceShipEntity, Modelship<SpaceShipEntity>> {
	public SpaceShipRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(Modelship.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(SpaceShipEntity entity) {
		return ResourceLocation.parse("robotica:textures/entities/ship.png");
	}

	private static final class AnimatedModel extends Modelship<SpaceShipEntity> {
		private final ModelPart root;
		private final HierarchicalModel animator = new HierarchicalModel<SpaceShipEntity>() {
			@Override
			public ModelPart root() {
				return root;
			}

			@Override
			public void setupAnim(SpaceShipEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
				this.root().getAllParts().forEach(ModelPart::resetPose);
				this.animate(entity.animationState0, shipAnimation.idle, ageInTicks, 1f);
				this.animateWalk(shipAnimation.movement, limbSwing, limbSwingAmount, 1f, 1f);
			}
		};

		public AnimatedModel(ModelPart root) {
			super(root);
			this.root = root;
		}

		@Override
		public void setupAnim(SpaceShipEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
			animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
	}
}