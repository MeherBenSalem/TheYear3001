package tn.nightbeam.robotica.network;

import tn.nightbeam.robotica.init.MenuAccessor;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import tn.nightbeam.robotica.RoboticaMod;

public final class FabricMenuStateUpdatePacket {
	public static final ResourceLocation ID = new ResourceLocation(RoboticaMod.MOD_ID, "menu_state_update");

	private FabricMenuStateUpdatePacket() {
	}

	public static FriendlyByteBuf create(int elementType, String name, Object elementState) {
		FriendlyByteBuf buffer = PacketByteBufs.create();
		buffer.writeInt(elementType);
		buffer.writeUtf(name);
		if (elementType == 0) {
			buffer.writeUtf((String) elementState);
		} else if (elementType == 1) {
			buffer.writeBoolean((boolean) elementState);
		}
		return buffer;
	}

	public static void handleServer(net.minecraft.server.level.ServerPlayer player, FriendlyByteBuf buffer) {
		int elementType = buffer.readInt();
		String name = buffer.readUtf();
		Object elementState = null;
		if (elementType == 0) {
			elementState = buffer.readUtf();
		} else if (elementType == 1) {
			elementState = buffer.readBoolean();
		}
		if (name.length() > 256 || elementState instanceof String string && string.length() > 8192) {
			return;
		}
		if (player.containerMenu instanceof MenuAccessor menu) {
			menu.getMenuState().put(elementType + ":" + name, elementState);
		}
	}
}
