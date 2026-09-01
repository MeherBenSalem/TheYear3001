package tn.nightbeam.robotica.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.fluid.OilFluid;

public class RoboticaModFluidsForge {
	public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(BuiltInRegistries.FLUID, RoboticaMod.MOD_ID);
	public static final DeferredHolder<Fluid, FlowingFluid> OIL = REGISTRY.register("oil", () -> new OilFluid.Source());
	public static final DeferredHolder<Fluid, FlowingFluid> FLOWING_OIL = REGISTRY.register("flowing_oil", () -> new OilFluid.Flowing());

	static {
		RoboticaModFluids.OIL = OIL;
		RoboticaModFluids.FLOWING_OIL = FLOWING_OIL;
	}
}
