package tn.nightbeam.robotica.network;

import tn.nightbeam.robotica.procedures.FlyCapsuleGoUpOnKeyReleasedProcedure;
import tn.nightbeam.robotica.procedures.FlyCapsuleGoDownOnKeyPressedProcedure;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FlyCapsuleGoDownMessage {
	int type, pressedms;

	public FlyCapsuleGoDownMessage(int type, int pressedms) {
		this.type = type;
		this.pressedms = pressedms;
	}

	public FlyCapsuleGoDownMessage(FriendlyByteBuf buffer) {
		this.type = buffer.readInt();
		this.pressedms = buffer.readInt();
	}

	public static void buffer(FlyCapsuleGoDownMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.type);
		buffer.writeInt(message.pressedms);
	}

	public static void handler(FlyCapsuleGoDownMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			pressAction(context.getSender(), message.type, message.pressedms);
		});
		context.setPacketHandled(true);
	}

	public static void pressAction(Player entity, int type, int pressedms) {
		Level world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(entity.blockPosition()))
			return;
		if (type == 0) {

			FlyCapsuleGoDownOnKeyPressedProcedure.execute(entity);
		}
		if (type == 1) {

			FlyCapsuleGoUpOnKeyReleasedProcedure.execute(entity);
		}
	}

}