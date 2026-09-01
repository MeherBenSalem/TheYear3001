package tn.nightbeam.robotica.init;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import tn.nightbeam.robotica.network.FabricFlyDownPacket;
import tn.nightbeam.robotica.network.FabricFlyForwardPacket;
import tn.nightbeam.robotica.network.FabricFlyUpPacket;
import tn.nightbeam.robotica.network.FlyCapsuleGoDownMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoForwardMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoUpMessage;

public final class RoboticaModFabricKeyMappings {
	public static final KeyMapping FLY_CAPSULE_GO_UP = new KeyMapping("key.robotica.fly_capsule_go_up", GLFW.GLFW_KEY_SPACE, "key.categories.theyear3001") {
		private boolean isDownOld;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ClientPlayNetworking.send(FabricFlyUpPacket.ID, FabricFlyUpPacket.encode(0, 0));
				FlyCapsuleGoUpMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				FLY_CAPSULE_GO_UP_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - FLY_CAPSULE_GO_UP_LASTPRESS);
				ClientPlayNetworking.send(FabricFlyUpPacket.ID, FabricFlyUpPacket.encode(1, dt));
				FlyCapsuleGoUpMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping FLY_CAPSULE_GO_DOWN = new KeyMapping("key.robotica.fly_capsule_go_down", GLFW.GLFW_KEY_LEFT_CONTROL, "key.categories.theyear3001") {
		private boolean isDownOld;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ClientPlayNetworking.send(FabricFlyDownPacket.ID, FabricFlyDownPacket.encode(0, 0));
				FlyCapsuleGoDownMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping FLY_CAPSULE_GO_FORWARD = new KeyMapping("key.robotica.fly_capsule_go_forward", GLFW.GLFW_KEY_W, "key.categories.theyear3001") {
		private boolean isDownOld;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ClientPlayNetworking.send(FabricFlyForwardPacket.ID, FabricFlyForwardPacket.encode(0, 0));
				FlyCapsuleGoForwardMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				FLY_CAPSULE_GO_FORWARD_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - FLY_CAPSULE_GO_FORWARD_LASTPRESS);
				ClientPlayNetworking.send(FabricFlyForwardPacket.ID, FabricFlyForwardPacket.encode(1, dt));
				FlyCapsuleGoForwardMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	private static long FLY_CAPSULE_GO_UP_LASTPRESS;
	private static long FLY_CAPSULE_GO_FORWARD_LASTPRESS;

	private RoboticaModFabricKeyMappings() {
	}

	public static void register() {
		KeyBindingHelper.registerKeyBinding(FLY_CAPSULE_GO_UP);
		KeyBindingHelper.registerKeyBinding(FLY_CAPSULE_GO_DOWN);
		KeyBindingHelper.registerKeyBinding(FLY_CAPSULE_GO_FORWARD);
	}
}
