package tn.nightbeam.robotica.init;

import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.fluid.types.OilFluidType;

public class RoboticaModFluidTypes {
	public static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, RoboticaMod.MOD_ID);
	public static final DeferredHolder<FluidType, FluidType> OIL_TYPE = REGISTRY.register("oil", () -> new OilFluidType());
}
