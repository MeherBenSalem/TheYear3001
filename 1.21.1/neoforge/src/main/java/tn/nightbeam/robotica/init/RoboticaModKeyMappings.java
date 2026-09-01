package tn.nightbeam.robotica.init;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import tn.nightbeam.robotica.RoboticaMod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;
import tn.nightbeam.robotica.neoforge.NeoForgeNetworking;
import tn.nightbeam.robotica.network.FlyCapsuleGoDownMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoForwardMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoUpMessage;

@EventBusSubscriber(modid = RoboticaMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class RoboticaModKeyMappings {
	public static final KeyMapping FLY_CAPSULE_GO_UP = new KeyMapping("key.robotica.fly_capsule_go_up", GLFW.GLFW_KEY_SPACE, "key.categories.theyear3001") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new NeoForgeNetworking.FlyCapsulePayload("up", 0, 0));
				FlyCapsuleGoUpMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				FLY_CAPSULE_GO_UP_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - FLY_CAPSULE_GO_UP_LASTPRESS);
				PacketDistributor.sendToServer(new NeoForgeNetworking.FlyCapsulePayload("up", 1, dt));
				FlyCapsuleGoUpMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping FLY_CAPSULE_GO_DOWN = new KeyMapping("key.robotica.fly_capsule_go_down", GLFW.GLFW_KEY_LEFT_CONTROL, "key.categories.theyear3001") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new NeoForgeNetworking.FlyCapsulePayload("down", 0, 0));
				FlyCapsuleGoDownMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				FLY_CAPSULE_GO_DOWN_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - FLY_CAPSULE_GO_DOWN_LASTPRESS);
				PacketDistributor.sendToServer(new NeoForgeNetworking.FlyCapsulePayload("down", 1, dt));
				FlyCapsuleGoDownMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping FLY_CAPSULE_GO_FORWARD = new KeyMapping("key.robotica.fly_capsule_go_forward", GLFW.GLFW_KEY_W, "key.categories.theyear3001") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new NeoForgeNetworking.FlyCapsulePayload("forward", 0, 0));
				FlyCapsuleGoForwardMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				FLY_CAPSULE_GO_FORWARD_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - FLY_CAPSULE_GO_FORWARD_LASTPRESS);
				PacketDistributor.sendToServer(new NeoForgeNetworking.FlyCapsulePayload("forward", 1, dt));
				FlyCapsuleGoForwardMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	private static long FLY_CAPSULE_GO_UP_LASTPRESS = 0;
	private static long FLY_CAPSULE_GO_DOWN_LASTPRESS = 0;
	private static long FLY_CAPSULE_GO_FORWARD_LASTPRESS = 0;

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(FLY_CAPSULE_GO_UP);
		event.register(FLY_CAPSULE_GO_DOWN);
		event.register(FLY_CAPSULE_GO_FORWARD);
	}

	@EventBusSubscriber(modid = RoboticaMod.MOD_ID, value = {Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			if (Minecraft.getInstance().screen == null) {
				FLY_CAPSULE_GO_UP.consumeClick();
				FLY_CAPSULE_GO_DOWN.consumeClick();
				FLY_CAPSULE_GO_FORWARD.consumeClick();
			}
		}
	}
}
