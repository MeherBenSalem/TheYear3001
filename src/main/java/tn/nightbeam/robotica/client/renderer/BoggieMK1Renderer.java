
package tn.nightbeam.robotica.client.renderer;

import tn.nightbeam.robotica.entity.model.BoggieMK1Model;
import tn.nightbeam.robotica.entity.BoggieMK1Entity;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class BoggieMK1Renderer extends GeoEntityRenderer<BoggieMK1Entity> {
	public BoggieMK1Renderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new BoggieMK1Model());
		this.shadowRadius = 0.5f;
	}

	@Override
	public RenderType getRenderType(BoggieMK1Entity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, BoggieMK1Entity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green,
			float blue, float alpha) {
		float scale = 1f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
