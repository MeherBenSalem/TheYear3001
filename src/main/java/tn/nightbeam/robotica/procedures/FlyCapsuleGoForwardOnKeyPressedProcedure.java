package tn.nightbeam.robotica.procedures;

import tn.nightbeam.robotica.entity.FlyingCapsuleMechEntity;

import net.minecraft.world.entity.Entity;

public class FlyCapsuleGoForwardOnKeyPressedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if ((entity.getVehicle()) instanceof FlyingCapsuleMechEntity) {
			(entity.getVehicle()).getPersistentData().putDouble("ForwardMovement", 1);
		}
	}
}
