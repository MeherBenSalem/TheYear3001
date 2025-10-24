package tn.nightbeam.robotica.entity.model;

import tn.nightbeam.robotica.entity.FlyingCapsuleMechEntity;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

public class FlyingCapsuleMechModel extends GeoModel<FlyingCapsuleMechEntity> {
	@Override
	public ResourceLocation getAnimationResource(FlyingCapsuleMechEntity entity) {
		return new ResourceLocation("robotica", "animations/ship.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(FlyingCapsuleMechEntity entity) {
		return new ResourceLocation("robotica", "geo/ship.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(FlyingCapsuleMechEntity entity) {
		return new ResourceLocation("robotica", "textures/entities/" + entity.getTexture() + ".png");
	}

}
