package tn.nightbeam.nextmillennium.entity.model;

import tn.nightbeam.nextmillennium.entity.FlyingCapsuleMechEntity;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

public class FlyingCapsuleMechModel extends GeoModel<FlyingCapsuleMechEntity> {
	@Override
	public ResourceLocation getAnimationResource(FlyingCapsuleMechEntity entity) {
		return new ResourceLocation("theyear3001", "animations/capsule.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(FlyingCapsuleMechEntity entity) {
		return new ResourceLocation("theyear3001", "geo/capsule.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(FlyingCapsuleMechEntity entity) {
		return new ResourceLocation("theyear3001", "textures/entities/" + entity.getTexture() + ".png");
	}

}
