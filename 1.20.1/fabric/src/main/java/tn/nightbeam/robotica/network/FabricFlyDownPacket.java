package tn.nightbeam.robotica.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import tn.nightbeam.robotica.RoboticaMod;

public final class FabricFlyDownPacket {
	public static final ResourceLocation ID = new ResourceLocation(RoboticaMod.MOD_ID, "fly_down");

	private FabricFlyDownPacket() {
	}

	public static FriendlyByteBuf encode(int pressAction, int pressedms) {
		FriendlyByteBuf buffer = PacketByteBufs.create();
		buffer.writeInt(pressAction);
		buffer.writeInt(pressedms);
		return buffer;
	}

	public static void handleServer(net.minecraft.server.level.ServerPlayer player, FriendlyByteBuf buffer) {
		FlyCapsuleGoDownMessage.pressAction(player, buffer.readInt(), buffer.readInt());
	}
}
