package tn.nightbeam.robotica.network;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import tn.nightbeam.robotica.procedures.FlyCapsuleGoUpOnKeyPressedProcedure;
import tn.nightbeam.robotica.procedures.FlyCapsuleGoUpOnKeyReleasedProcedure;

public final class FlyCapsuleGoUpMessage {
	private FlyCapsuleGoUpMessage() {
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
			FlyCapsuleGoUpOnKeyPressedProcedure.execute(entity);
		}
		if (type == 1) {
			FlyCapsuleGoUpOnKeyReleasedProcedure.execute(entity);
		}
	}
}
