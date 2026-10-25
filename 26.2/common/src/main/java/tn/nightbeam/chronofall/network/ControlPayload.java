package tn.nightbeam.chronofall.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import tn.nightbeam.chronofall.Chronofall;

/** Client to server: the control bits of {@link Control}. */
public record ControlPayload(int flags) implements CustomPacketPayload {
    public static final Type<ControlPayload> TYPE = new Type<>(Chronofall.id("control"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ControlPayload> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT,
            ControlPayload::flags, ControlPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
