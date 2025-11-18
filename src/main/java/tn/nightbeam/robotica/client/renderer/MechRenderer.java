package tn.nightbeam.robotica.client.renderer;

import tn.nightbeam.robotica.entity.MechEntity;
import tn.nightbeam.robotica.client.model.animations.mechAnimation;
import tn.nightbeam.robotica.client.model.Modelmech;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HierarchicalModel;

public class MechRenderer extends MobRenderer<MechEntity, Modelmech<MechEntity>> {
	public MechRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(Modelmech.LAYER_LOCATION)), 0f);
	}

	@Override
	public ResourceLocation getTextureLocation(MechEntity entity) {
		return ResourceLocation.parse("robotica:textures/entities/r1d1.png");
	}

	private static final class AnimatedModel extends Modelmech<MechEntity> {
		private final ModelPart root;
		private final HierarchicalModel animator = new HierarchicalModel<MechEntity>() {
			@Override
			public ModelPart root() {
				return root;
			}

			@Override
			public void setupAnim(MechEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
				this.root().getAllParts().forEach(ModelPart::resetPose);
				this.animate(entity.animationState0, mechAnimation.idle, ageInTicks, 1f);
				this.animateWalk(mechAnimation.walk, limbSwing, limbSwingAmount, 1f, 1f);
				this.animate(entity.animationState2, mechAnimation.attack, ageInTicks, 1f);
			}
		};

		public AnimatedModel(ModelPart root) {
			super(root);
			this.root = root;
		}

		@Override
		public void setupAnim(MechEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
			animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
	}
}