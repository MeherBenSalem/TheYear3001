package tn.nightbeam.robotica.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import tn.nightbeam.robotica.RoboticaMod;

public class RoboticaModSoundsFabric {
	public static void register() {
		SoundEvent robotDie = register("robot_die");
		SoundEvent robotAmbient = register("robot_ambient");
		SoundEvent alienRobotAmbient = register("alient_robot_ambient");
		RoboticaModSounds.ROBOT_DIE = () -> robotDie;
		RoboticaModSounds.ROBOT_AMBIENT = () -> robotAmbient;
		RoboticaModSounds.ALIENT_ROBOT_AMBIENT = () -> alienRobotAmbient;
	}

	private static SoundEvent register(String name) {
		ResourceLocation id = new ResourceLocation(RoboticaMod.MOD_ID, name);
		SoundEvent event = SoundEvent.createVariableRangeEvent(id);
		return Registry.register(BuiltInRegistries.SOUND_EVENT, id, event);
	}
}
