
package tn.nightbeam.nextmillennium.client.renderer;

import tn.nightbeam.nextmillennium.entity.model.SentinelMK1Model;
import tn.nightbeam.nextmillennium.entity.SentinelMK1Entity;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class SentinelMK1Renderer extends GeoEntityRenderer<SentinelMK1Entity> {
	public SentinelMK1Renderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new SentinelMK1Model());
		this.shadowRadius = 0.5f;
	}

	@Override
	public RenderType getRenderType(SentinelMK1Entity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, SentinelMK1Entity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green,
			float blue, float alpha) {
		float scale = 1f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
