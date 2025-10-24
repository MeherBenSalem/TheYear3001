
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.RoboticaMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

public class RoboticaModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RoboticaMod.MODID);
	public static final RegistryObject<SoundEvent> ROBOT_DIE = REGISTRY.register("robot_die", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("robotica", "robot_die")));
	public static final RegistryObject<SoundEvent> ROBOT_AMBIENT = REGISTRY.register("robot_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("robotica", "robot_ambient")));
	public static final RegistryObject<SoundEvent> ALIENT_ROBOT_AMBIENT = REGISTRY.register("alient_robot_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("robotica", "alient_robot_ambient")));
	public static final RegistryObject<SoundEvent> ROBOT_BIRDS = REGISTRY.register("robot_birds", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("robotica", "robot_birds")));
}
