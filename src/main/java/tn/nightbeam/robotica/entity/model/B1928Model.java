package tn.nightbeam.robotica.entity.model;

import tn.nightbeam.robotica.entity.B1928Entity;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

public class B1928Model extends GeoModel<B1928Entity> {
	@Override
	public ResourceLocation getAnimationResource(B1928Entity entity) {
		return new ResourceLocation("robotica", "animations/b-1928.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(B1928Entity entity) {
		return new ResourceLocation("robotica", "geo/b-1928.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(B1928Entity entity) {
		return new ResourceLocation("robotica", "textures/entities/" + entity.getTexture() + ".png");
	}

}
