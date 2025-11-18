package tn.nightbeam.robotica.client.renderer;

import tn.nightbeam.robotica.entity.BoggieEntity;
import tn.nightbeam.robotica.client.model.animations.boogieAnimation;
import tn.nightbeam.robotica.client.model.Modelboogie;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HierarchicalModel;

public class BoggieRenderer extends MobRenderer<BoggieEntity, Modelboogie<BoggieEntity>> {
	public BoggieRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(Modelboogie.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(BoggieEntity entity) {
		return ResourceLocation.parse("robotica:textures/entities/boogie_java.png");
	}

	private static final class AnimatedModel extends Modelboogie<BoggieEntity> {
		private final ModelPart root;
		private final HierarchicalModel animator = new HierarchicalModel<BoggieEntity>() {
			@Override
			public ModelPart root() {
				return root;
			}

			@Override
			public void setupAnim(BoggieEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
				this.root().getAllParts().forEach(ModelPart::resetPose);
				this.animate(entity.animationState0, boogieAnimation.idle, ageInTicks, 1f);
				this.animateWalk(boogieAnimation.walk, limbSwing, limbSwingAmount, 1f, 1f);
				this.animate(entity.animationState2, boogieAnimation.attack, ageInTicks, 1f);
			}
		};

		public AnimatedModel(ModelPart root) {
			super(root);
			this.root = root;
		}

		@Override
		public void setupAnim(BoggieEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
			animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
	}
}