
/*
 * MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.nextmillennium.init;

import tn.nightbeam.nextmillennium.fluid.types.OilFluidType;
import tn.nightbeam.nextmillennium.Theyear3001Mod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fluids.FluidType;

public class Theyear3001ModFluidTypes {
	public static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Theyear3001Mod.MODID);
	public static final RegistryObject<FluidType> OIL_TYPE = REGISTRY.register("oil", () -> new OilFluidType());
}
