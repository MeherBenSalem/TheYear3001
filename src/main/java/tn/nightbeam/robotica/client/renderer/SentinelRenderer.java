package tn.nightbeam.robotica.client.renderer;

import tn.nightbeam.robotica.entity.SentinelEntity;
import tn.nightbeam.robotica.client.model.animations.sentinelAnimation;
import tn.nightbeam.robotica.client.model.Modelsentinel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HierarchicalModel;

public class SentinelRenderer extends MobRenderer<SentinelEntity, Modelsentinel<SentinelEntity>> {
	public SentinelRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(Modelsentinel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(SentinelEntity entity) {
		return ResourceLocation.parse("robotica:textures/entities/robot_1.png");
	}

	private static final class AnimatedModel extends Modelsentinel<SentinelEntity> {
		private final ModelPart root;
		private final HierarchicalModel animator = new HierarchicalModel<SentinelEntity>() {
			@Override
			public ModelPart root() {
				return root;
			}

			@Override
			public void setupAnim(SentinelEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
				this.root().getAllParts().forEach(ModelPart::resetPose);
				this.animate(entity.animationState0, sentinelAnimation.idle, ageInTicks, 1f);
				this.animateWalk(sentinelAnimation.walk, limbSwing, limbSwingAmount, 1f, 1f);
				this.animate(entity.animationState2, sentinelAnimation.attack, ageInTicks, 1f);
			}
		};

		public AnimatedModel(ModelPart root) {
			super(root);
			this.root = root;
		}

		@Override
		public void setupAnim(SentinelEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
			animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
	}
}