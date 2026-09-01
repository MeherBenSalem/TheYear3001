package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.RoboticaModNeoForge;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;

public class RoboticaModSoundsNeoForge {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, RoboticaMod.MOD_ID);
	public static final DeferredHolder<SoundEvent, SoundEvent> ROBOT_DIE = REGISTRY.register("robot_die", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("robotica", "robot_die")));
	public static final DeferredHolder<SoundEvent, SoundEvent> ROBOT_AMBIENT = REGISTRY.register("robot_ambient", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("robotica", "robot_ambient")));
	public static final DeferredHolder<SoundEvent, SoundEvent> ALIENT_ROBOT_AMBIENT = REGISTRY.register("alient_robot_ambient", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("robotica", "alient_robot_ambient")));


	static {
		RoboticaModSounds.ROBOT_DIE = ROBOT_DIE;
		RoboticaModSounds.ROBOT_AMBIENT = ROBOT_AMBIENT;
		RoboticaModSounds.ALIENT_ROBOT_AMBIENT = ALIENT_ROBOT_AMBIENT;
	}
}
