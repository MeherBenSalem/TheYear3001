package tn.nightbeam.nextmillennium.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;

public class FlyingCapsuleMechOnEntityTickUpdateProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double particleRadius = 0;
		double particleAmount = 0;
		if (entity.getPersistentData().getDouble("VerticalMovement") == 1) {
			entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x()), 0.3, (entity.getDeltaMovement().z())));
		} else if (entity.getPersistentData().getDouble("VerticalMovement") == -1) {
			entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x()), (-0.3), (entity.getDeltaMovement().z())));
		}
		if (!entity.onGround()) {
			if (entity.getPersistentData().getDouble("ForwardMovement") == 1) {
				entity.setDeltaMovement(new Vec3((entity.getLookAngle().x * 1), (entity.getXRot() * (-0.01)), (entity.getLookAngle().z * 1)));
			}
		}
	}
}
