package tn.nightbeam.robotica.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import tn.nightbeam.robotica.RoboticaMod;

public record FabricFlyUpPayload(int pressAction, int pressedms) implements CustomPacketPayload {
	public static final Type<FabricFlyUpPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(RoboticaMod.MOD_ID, "fly_up"));
	public static final StreamCodec<FriendlyByteBuf, FabricFlyUpPayload> CODEC = StreamCodec.of(
			(buf, payload) -> {
				buf.writeInt(payload.pressAction);
				buf.writeInt(payload.pressedms);
			},
			buf -> new FabricFlyUpPayload(buf.readInt(), buf.readInt()));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
