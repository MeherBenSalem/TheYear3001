package tn.nightbeam.robotica.procedures;

import tn.nightbeam.robotica.entity.SpaceShipEntity;

import net.minecraft.world.entity.Entity;

public class FlyCapsuleGoUpOnKeyReleasedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if ((entity.getVehicle()) instanceof SpaceShipEntity) {
			(entity.getVehicle()).getPersistentData().putDouble("VerticalMovement", 0);
		}
	}
}