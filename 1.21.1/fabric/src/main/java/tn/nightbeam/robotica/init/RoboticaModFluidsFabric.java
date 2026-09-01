package tn.nightbeam.robotica.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FlowingFluid;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.block.OilBlock;
import tn.nightbeam.robotica.fluid.OilFluid;

public final class RoboticaModFluidsFabric {
	private RoboticaModFluidsFabric() {
	}

	public static void register() {
		ResourceLocation oilId = ResourceLocation.fromNamespaceAndPath(RoboticaMod.MOD_ID, "oil");
		ResourceLocation flowingOilId = ResourceLocation.fromNamespaceAndPath(RoboticaMod.MOD_ID, "flowing_oil");
		ResourceLocation oilBlockId = ResourceLocation.fromNamespaceAndPath(RoboticaMod.MOD_ID, "oil");

		FlowingFluid oil = Registry.register(BuiltInRegistries.FLUID, oilId, new OilFluid.Source());
		FlowingFluid flowingOil = Registry.register(BuiltInRegistries.FLUID, flowingOilId, new OilFluid.Flowing());
		RoboticaModFluids.OIL = () -> oil;
		RoboticaModFluids.FLOWING_OIL = () -> flowingOil;

		Block oilBlock = Registry.register(BuiltInRegistries.BLOCK, oilBlockId, new OilBlock());
		RoboticaModBlocks.OIL = () -> oilBlock;
	}
}
