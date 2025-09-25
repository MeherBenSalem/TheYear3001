package tn.nightbeam.nextmillennium.entity.model;

import tn.nightbeam.nextmillennium.entity.RobotMK1Entity;

import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.constant.DataTickets;

import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;

public class RobotMK1Model extends GeoModel<RobotMK1Entity> {
	@Override
	public ResourceLocation getAnimationResource(RobotMK1Entity entity) {
		return new ResourceLocation("theyear3001", "animations/robot_1.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(RobotMK1Entity entity) {
		return new ResourceLocation("theyear3001", "geo/robot_1.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(RobotMK1Entity entity) {
		return new ResourceLocation("theyear3001", "textures/entities/" + entity.getTexture() + ".png");
	}

	@Override
	public void setCustomAnimations(RobotMK1Entity animatable, long instanceId, AnimationState animationState) {
		CoreGeoBone head = getAnimationProcessor().getBone("h_head");
		if (head != null) {
			EntityModelData entityData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);
			head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
			head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
		}

	}
}
