
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.nextmillennium.init;

import tn.nightbeam.nextmillennium.Theyear3001Mod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

public class Theyear3001ModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Theyear3001Mod.MODID);
	public static final RegistryObject<SoundEvent> ROBOT_DIE = REGISTRY.register("robot_die", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("theyear3001", "robot_die")));
	public static final RegistryObject<SoundEvent> ROBOT_AMBIENT = REGISTRY.register("robot_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("theyear3001", "robot_ambient")));
	public static final RegistryObject<SoundEvent> ALIENT_ROBOT_AMBIENT = REGISTRY.register("alient_robot_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("theyear3001", "alient_robot_ambient")));
	public static final RegistryObject<SoundEvent> ROBOT_BIRDS = REGISTRY.register("robot_birds", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("theyear3001", "robot_birds")));
}
