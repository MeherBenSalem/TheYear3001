package tn.nightbeam.robotica.procedures;

import net.minecraft.world.entity.Entity;

public class CheckIdleAnimProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		return Math.hypot(entity.getDeltaMovement().x(), entity.getDeltaMovement().z()) == 0;
	}
}