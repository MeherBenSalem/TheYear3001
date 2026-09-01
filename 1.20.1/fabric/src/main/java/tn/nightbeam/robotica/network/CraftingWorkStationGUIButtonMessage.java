package tn.nightbeam.robotica.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import tn.nightbeam.robotica.procedures.CraftExtendedProcedure;

public final class CraftingWorkStationGUIButtonMessage {
	private CraftingWorkStationGUIButtonMessage() {
	}

	public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
		Level world = entity.level();
		if (!world.hasChunkAt(new BlockPos(x, y, z))) {
			return;
		}
		if (buttonID == 0) {
			CraftExtendedProcedure.execute(entity);
		}
	}
}
