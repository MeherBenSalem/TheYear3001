package tn.nightbeam.nextmillennium.entity.model;

import tn.nightbeam.nextmillennium.entity.MechMK1Entity;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

public class MechMK1Model extends GeoModel<MechMK1Entity> {
	@Override
	public ResourceLocation getAnimationResource(MechMK1Entity entity) {
		return new ResourceLocation("theyear3001", "animations/r1d1.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(MechMK1Entity entity) {
		return new ResourceLocation("theyear3001", "geo/r1d1.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(MechMK1Entity entity) {
		return new ResourceLocation("theyear3001", "textures/entities/" + entity.getTexture() + ".png");
	}

}
