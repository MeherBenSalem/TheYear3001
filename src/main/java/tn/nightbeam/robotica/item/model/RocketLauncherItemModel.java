package tn.nightbeam.robotica.item.model;

import tn.nightbeam.robotica.item.RocketLauncherItem;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

public class RocketLauncherItemModel extends GeoModel<RocketLauncherItem> {
	@Override
	public ResourceLocation getAnimationResource(RocketLauncherItem animatable) {
		return new ResourceLocation("robotica", "animations/rocket.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(RocketLauncherItem animatable) {
		return new ResourceLocation("robotica", "geo/rocket.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(RocketLauncherItem animatable) {
		return new ResourceLocation("robotica", "textures/item/rocket.png");
	}
}
