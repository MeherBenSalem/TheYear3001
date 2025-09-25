package tn.nightbeam.nextmillennium.entity.model;

import tn.nightbeam.nextmillennium.entity.B2050Entity;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

public class B2050Model extends GeoModel<B2050Entity> {
	@Override
	public ResourceLocation getAnimationResource(B2050Entity entity) {
		return new ResourceLocation("theyear3001", "animations/b-2050.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(B2050Entity entity) {
		return new ResourceLocation("theyear3001", "geo/b-2050.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(B2050Entity entity) {
		return new ResourceLocation("theyear3001", "textures/entities/" + entity.getTexture() + ".png");
	}

}
