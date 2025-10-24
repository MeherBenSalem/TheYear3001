
/*
 * MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.fluid.types.OilFluidType;
import tn.nightbeam.robotica.RoboticaMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fluids.FluidType;

public class RoboticaModFluidTypes {
	public static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, RoboticaMod.MODID);
	public static final RegistryObject<FluidType> OIL_TYPE = REGISTRY.register("oil", () -> new OilFluidType());
}
