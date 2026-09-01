package tn.nightbeam.robotica.procedures;

import tn.nightbeam.robotica.util.RoboticaEntityData;

import tn.nightbeam.robotica.entity.SpaceShipEntity;

import net.minecraft.world.entity.Entity;

public class FlyCapsuleGoForwardOnKeyReleasedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if ((entity.getVehicle()) instanceof SpaceShipEntity) {
			RoboticaEntityData.get(entity.getVehicle()).putDouble("ForwardMovement", 0);
		}
	}
}