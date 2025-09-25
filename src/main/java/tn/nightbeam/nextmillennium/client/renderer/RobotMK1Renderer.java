
package tn.nightbeam.nextmillennium.client.renderer;

import tn.nightbeam.nextmillennium.entity.model.RobotMK1Model;
import tn.nightbeam.nextmillennium.entity.RobotMK1Entity;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class RobotMK1Renderer extends GeoEntityRenderer<RobotMK1Entity> {
	public RobotMK1Renderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new RobotMK1Model());
		this.shadowRadius = 0.5f;
	}

	@Override
	public RenderType getRenderType(RobotMK1Entity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, RobotMK1Entity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green,
			float blue, float alpha) {
		float scale = 1f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
