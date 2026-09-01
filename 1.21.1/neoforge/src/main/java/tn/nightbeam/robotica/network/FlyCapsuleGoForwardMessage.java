package tn.nightbeam.robotica.network;

import tn.nightbeam.robotica.procedures.FlyCapsuleGoForwardOnKeyPressedProcedure;
import tn.nightbeam.robotica.procedures.FlyCapsuleGoForwardOnKeyReleasedProcedure;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FlyCapsuleGoForwardMessage {
	public static void pressAction(Player entity, int type, int pressedms) {
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
