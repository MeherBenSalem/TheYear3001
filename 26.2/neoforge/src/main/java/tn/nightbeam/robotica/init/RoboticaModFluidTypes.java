package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.fluid.types.OilFluidType;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.RoboticaModNeoForge;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.fluids.FluidType;

public class RoboticaModFluidTypes {
	public static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.FLUID_TYPES, RoboticaMod.MOD_ID);
	public static final DeferredHolder<FluidType, FluidType> OIL_TYPE = REGISTRY.register("oil", () -> new OilFluidType());
}