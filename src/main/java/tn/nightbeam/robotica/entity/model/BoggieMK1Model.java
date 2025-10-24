package tn.nightbeam.robotica.entity.model;

import tn.nightbeam.robotica.entity.BoggieMK1Entity;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

public class BoggieMK1Model extends GeoModel<BoggieMK1Entity> {
	@Override
	public ResourceLocation getAnimationResource(BoggieMK1Entity entity) {
		return new ResourceLocation("robotica", "animations/boggie.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BoggieMK1Entity entity) {
		return new ResourceLocation("robotica", "geo/boggie.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BoggieMK1Entity entity) {
		return new ResourceLocation("robotica", "textures/entities/" + entity.getTexture() + ".png");
	}

}
