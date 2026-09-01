package tn.nightbeam.robotica.network;

import tn.nightbeam.robotica.procedures.FlyCapsuleGoDownOnKeyPressedProcedure;
import tn.nightbeam.robotica.procedures.FlyCapsuleGoUpOnKeyReleasedProcedure;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FlyCapsuleGoDownMessage {
	public static void pressAction(Player entity, int type, int pressedms) {
		Level world = entity.level();
		if (!world.hasChunkAt(entity.blockPosition())) {
			return;
		}
		if (type == 0) {
			FlyCapsuleGoDownOnKeyPressedProcedure.execute(entity);
		}
		if (type == 1) {
			FlyCapsuleGoUpOnKeyReleasedProcedure.execute(entity);
		}
	}
}
