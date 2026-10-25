package tn.nightbeam.chronofall.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import tn.nightbeam.chronofall.Chronofall;

/** Client to server: the control bits of {@link Control}. */
public record ControlPayload(int flags) {
    public static final ResourceLocation ID = Chronofall.id("control");

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(flags);
    }

    public static ControlPayload read(FriendlyByteBuf buf) {
        return new ControlPayload(buf.readVarInt());
    }
}
