package tn.nightbeam.robotica.network;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import tn.nightbeam.robotica.procedures.FlyCapsuleGoDownOnKeyPressedProcedure;
import tn.nightbeam.robotica.procedures.FlyCapsuleGoUpOnKeyReleasedProcedure;

public final class FlyCapsuleGoDownMessage {
	private FlyCapsuleGoDownMessage() {
	}

	public static void pressAction(Player entity, int type, int pressedms) {
		if (entity == null) {
			return;
		}
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
