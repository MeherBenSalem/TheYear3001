package tn.nightbeam.robotica.procedures;

import net.minecraft.world.entity.Entity;

public class MechMK1RightClickedOnEntityProcedure {
	public static void execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		UpgradeCardsProcedure.execute(entity, sourceentity);
	}
}
