package tn.nightbeam.robotica.init;

import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import tn.nightbeam.robotica.network.FabricFlyDownPayload;
import tn.nightbeam.robotica.network.FabricFlyForwardPayload;
import tn.nightbeam.robotica.network.FabricFlyUpPayload;
import tn.nightbeam.robotica.network.FlyCapsuleGoDownMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoForwardMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoUpMessage;

public final class RoboticaModFabricKeyMappings {
	public static final KeyMapping FLY_CAPSULE_GO_UP = new KeyMapping("key.robotica.fly_capsule_go_up", GLFW.GLFW_KEY_SPACE, KeyMapping.Category.MISC) {
		private boolean isDownOld;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ClientPlayNetworking.send(new FabricFlyUpPayload(0, 0));
				FlyCapsuleGoUpMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				FLY_CAPSULE_GO_UP_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - FLY_CAPSULE_GO_UP_LASTPRESS);
				ClientPlayNetworking.send(new FabricFlyUpPayload(1, dt));
				FlyCapsuleGoUpMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping FLY_CAPSULE_GO_DOWN = new KeyMapping("key.robotica.fly_capsule_go_down", GLFW.GLFW_KEY_LEFT_CONTROL, KeyMapping.Category.MISC) {
		private boolean isDownOld;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ClientPlayNetworking.send(new FabricFlyDownPayload(0, 0));
				FlyCapsuleGoDownMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping FLY_CAPSULE_GO_FORWARD = new KeyMapping("key.robotica.fly_capsule_go_forward", GLFW.GLFW_KEY_W, KeyMapping.Category.MISC) {
		private boolean isDownOld;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ClientPlayNetworking.send(new FabricFlyForwardPayload(0, 0));
				FlyCapsuleGoForwardMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				FLY_CAPSULE_GO_FORWARD_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - FLY_CAPSULE_GO_FORWARD_LASTPRESS);
				ClientPlayNetworking.send(new FabricFlyForwardPayload(1, dt));
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
		KeyMappingHelper.registerKeyMapping(FLY_CAPSULE_GO_UP);
		KeyMappingHelper.registerKeyMapping(FLY_CAPSULE_GO_DOWN);
		KeyMappingHelper.registerKeyMapping(FLY_CAPSULE_GO_FORWARD);
	}
}
