package tn.nightbeam.robotica.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import tn.nightbeam.robotica.RoboticaMod;

public final class FabricCraftingButtonPacket {
	public static final ResourceLocation ID = new ResourceLocation(RoboticaMod.MOD_ID, "crafting_button");

	private FabricCraftingButtonPacket() {
	}

	public static FriendlyByteBuf encode(int buttonID, int x, int y, int z) {
		FriendlyByteBuf buffer = PacketByteBufs.create();
		buffer.writeInt(buttonID);
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		return buffer;
	}

	public static void handleServer(net.minecraft.server.level.ServerPlayer player, FriendlyByteBuf buffer) {
		CraftingWorkStationGUIButtonMessage.handleButtonAction(player, buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt());
	}
}
