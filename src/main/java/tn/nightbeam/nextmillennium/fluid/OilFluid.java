
package tn.nightbeam.nextmillennium.fluid;

import tn.nightbeam.nextmillennium.init.Theyear3001ModItems;
import tn.nightbeam.nextmillennium.init.Theyear3001ModFluids;
import tn.nightbeam.nextmillennium.init.Theyear3001ModFluidTypes;
import tn.nightbeam.nextmillennium.init.Theyear3001ModBlocks;

import net.minecraftforge.fluids.ForgeFlowingFluid;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.LiquidBlock;

public abstract class OilFluid extends ForgeFlowingFluid {
	public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(() -> Theyear3001ModFluidTypes.OIL_TYPE.get(), () -> Theyear3001ModFluids.OIL.get(), () -> Theyear3001ModFluids.FLOWING_OIL.get())
			.explosionResistance(100f).tickRate(3).bucket(() -> Theyear3001ModItems.OIL_BUCKET.get()).block(() -> (LiquidBlock) Theyear3001ModBlocks.OIL.get());

	private OilFluid() {
		super(PROPERTIES);
	}

	public static class Source extends OilFluid {
		public int getAmount(FluidState state) {
			return 8;
		}

		public boolean isSource(FluidState state) {
			return true;
		}
	}

	public static class Flowing extends OilFluid {
		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

		public int getAmount(FluidState state) {
			return state.getValue(LEVEL);
		}

		public boolean isSource(FluidState state) {
			return false;
		}
	}
}
