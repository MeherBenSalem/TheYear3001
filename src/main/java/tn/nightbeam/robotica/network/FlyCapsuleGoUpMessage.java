package tn.nightbeam.robotica.network;

import tn.nightbeam.robotica.procedures.FlyCapsuleGoUpOnKeyReleasedProcedure;
import tn.nightbeam.robotica.procedures.FlyCapsuleGoUpOnKeyPressedProcedure;
import tn.nightbeam.robotica.RoboticaMod;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FlyCapsuleGoUpMessage {
	int type, pressedms;

	public FlyCapsuleGoUpMessage(int type, int pressedms) {
		this.type = type;
		this.pressedms = pressedms;
	}

	public FlyCapsuleGoUpMessage(FriendlyByteBuf buffer) {
		this.type = buffer.readInt();
		this.pressedms = buffer.readInt();
	}

	public static void buffer(FlyCapsuleGoUpMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.type);
		buffer.writeInt(message.pressedms);
	}

	public static void handler(FlyCapsuleGoUpMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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

			FlyCapsuleGoUpOnKeyPressedProcedure.execute(entity);
		}
		if (type == 1) {

			FlyCapsuleGoUpOnKeyReleasedProcedure.execute(entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		RoboticaMod.addNetworkMessage(FlyCapsuleGoUpMessage.class, FlyCapsuleGoUpMessage::buffer, FlyCapsuleGoUpMessage::new, FlyCapsuleGoUpMessage::handler);
	}
}