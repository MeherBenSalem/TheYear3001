package tn.nightbeam.robotica.entity.model;

import tn.nightbeam.robotica.entity.B1990Entity;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

public class B1990Model extends GeoModel<B1990Entity> {
	@Override
	public ResourceLocation getAnimationResource(B1990Entity entity) {
		return new ResourceLocation("robotica", "animations/b1990.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(B1990Entity entity) {
		return new ResourceLocation("robotica", "geo/b1990.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(B1990Entity entity) {
		return new ResourceLocation("robotica", "textures/entities/" + entity.getTexture() + ".png");
	}

}
