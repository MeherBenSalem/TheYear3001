package tn.nightbeam.robotica.init;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import tn.nightbeam.robotica.RoboticaMod;

public final class RoboticaModKeyMappings {
	public static final KeyMapping FLY_CAPSULE_GO_UP = new KeyMapping("key.robotica.fly_capsule_go_up", GLFW.GLFW_KEY_SPACE, KeyMapping.Category.MISC);
	public static final KeyMapping FLY_CAPSULE_GO_DOWN = new KeyMapping("key.robotica.fly_capsule_go_down", GLFW.GLFW_KEY_LEFT_CONTROL, KeyMapping.Category.MISC);
	public static final KeyMapping FLY_CAPSULE_GO_FORWARD = new KeyMapping("key.robotica.fly_capsule_go_forward", GLFW.GLFW_KEY_W, KeyMapping.Category.MISC);

	private RoboticaModKeyMappings() {
	}
}
