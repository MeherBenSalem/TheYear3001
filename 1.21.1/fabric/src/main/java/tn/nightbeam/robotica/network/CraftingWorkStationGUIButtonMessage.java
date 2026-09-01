package tn.nightbeam.robotica.network;

import tn.nightbeam.robotica.procedures.CraftExtendedProcedure;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CraftingWorkStationGUIButtonMessage {
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
