package tn.nightbeam.nextmillennium.entity.model;

import tn.nightbeam.nextmillennium.entity.B1990Entity;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

public class B1990Model extends GeoModel<B1990Entity> {
	@Override
	public ResourceLocation getAnimationResource(B1990Entity entity) {
		return new ResourceLocation("theyear3001", "animations/b1990.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(B1990Entity entity) {
		return new ResourceLocation("theyear3001", "geo/b1990.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(B1990Entity entity) {
		return new ResourceLocation("theyear3001", "textures/entities/" + entity.getTexture() + ".png");
	}

}
