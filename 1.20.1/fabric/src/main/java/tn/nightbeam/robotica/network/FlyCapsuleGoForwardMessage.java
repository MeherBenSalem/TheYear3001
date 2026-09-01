package tn.nightbeam.robotica.network;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import tn.nightbeam.robotica.procedures.FlyCapsuleGoForwardOnKeyPressedProcedure;
import tn.nightbeam.robotica.procedures.FlyCapsuleGoForwardOnKeyReleasedProcedure;

public final class FlyCapsuleGoForwardMessage {
	private FlyCapsuleGoForwardMessage() {
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
			FlyCapsuleGoForwardOnKeyPressedProcedure.execute(entity);
		}
		if (type == 1) {
			FlyCapsuleGoForwardOnKeyReleasedProcedure.execute(entity);
		}
	}
}
