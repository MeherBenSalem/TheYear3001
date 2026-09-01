package tn.nightbeam.robotica.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import tn.nightbeam.robotica.RoboticaMod;

public record FabricCraftingButtonPayload(int buttonID, int x, int y, int z) implements CustomPacketPayload {
	public static final Type<FabricCraftingButtonPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(RoboticaMod.MOD_ID, "crafting_button"));
	public static final StreamCodec<FriendlyByteBuf, FabricCraftingButtonPayload> CODEC = StreamCodec.of(
			(buf, payload) -> {
				buf.writeInt(payload.buttonID);
				buf.writeInt(payload.x);
				buf.writeInt(payload.y);
				buf.writeInt(payload.z);
			},
			buf -> new FabricCraftingButtonPayload(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt()));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
