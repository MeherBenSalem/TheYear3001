package tn.nightbeam.robotica.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import tn.nightbeam.robotica.RoboticaMod;

public record FabricFlyDownPayload(int pressAction, int pressedms) implements CustomPacketPayload {
	public static final Type<FabricFlyDownPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(RoboticaMod.MOD_ID, "fly_down"));
	public static final StreamCodec<FriendlyByteBuf, FabricFlyDownPayload> CODEC = StreamCodec.of(
			(buf, payload) -> {
				buf.writeInt(payload.pressAction);
				buf.writeInt(payload.pressedms);
			},
			buf -> new FabricFlyDownPayload(buf.readInt(), buf.readInt()));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
