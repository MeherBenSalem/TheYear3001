package tn.nightbeam.robotica.entity.model;

import tn.nightbeam.robotica.entity.MechMK1Entity;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

public class MechMK1Model extends GeoModel<MechMK1Entity> {
	@Override
	public ResourceLocation getAnimationResource(MechMK1Entity entity) {
		return new ResourceLocation("robotica", "animations/r1d1.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(MechMK1Entity entity) {
		return new ResourceLocation("robotica", "geo/r1d1.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(MechMK1Entity entity) {
		return new ResourceLocation("robotica", "textures/entities/" + entity.getTexture() + ".png");
	}

}
