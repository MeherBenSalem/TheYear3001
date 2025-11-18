/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.network.FlyCapsuleGoUpMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoForwardMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoDownMessage;
import tn.nightbeam.robotica.RoboticaMod;

import org.lwjgl.glfw.GLFW;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class RoboticaModKeyMappings {
	public static final KeyMapping FLY_CAPSULE_GO_UP = new KeyMapping("key.robotica.fly_capsule_go_up", GLFW.GLFW_KEY_SPACE, "key.categories.theyear3001") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				RoboticaMod.PACKET_HANDLER.sendToServer(new FlyCapsuleGoUpMessage(0, 0));
				FlyCapsuleGoUpMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				FLY_CAPSULE_GO_UP_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - FLY_CAPSULE_GO_UP_LASTPRESS);
				RoboticaMod.PACKET_HANDLER.sendToServer(new FlyCapsuleGoUpMessage(1, dt));
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
				RoboticaMod.PACKET_HANDLER.sendToServer(new FlyCapsuleGoDownMessage(0, 0));
				FlyCapsuleGoDownMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				FLY_CAPSULE_GO_DOWN_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - FLY_CAPSULE_GO_DOWN_LASTPRESS);
				RoboticaMod.PACKET_HANDLER.sendToServer(new FlyCapsuleGoDownMessage(1, dt));
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
				RoboticaMod.PACKET_HANDLER.sendToServer(new FlyCapsuleGoForwardMessage(0, 0));
				FlyCapsuleGoForwardMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				FLY_CAPSULE_GO_FORWARD_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - FLY_CAPSULE_GO_FORWARD_LASTPRESS);
				RoboticaMod.PACKET_HANDLER.sendToServer(new FlyCapsuleGoForwardMessage(1, dt));
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

	@Mod.EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getInstance().screen == null) {
				FLY_CAPSULE_GO_UP.consumeClick();
				FLY_CAPSULE_GO_DOWN.consumeClick();
				FLY_CAPSULE_GO_FORWARD.consumeClick();
			}
		}
	}
}