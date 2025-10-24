
package tn.nightbeam.robotica.fluid;

import tn.nightbeam.robotica.init.RoboticaModItems;
import tn.nightbeam.robotica.init.RoboticaModFluids;
import tn.nightbeam.robotica.init.RoboticaModFluidTypes;
import tn.nightbeam.robotica.init.RoboticaModBlocks;

import net.minecraftforge.fluids.ForgeFlowingFluid;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.LiquidBlock;

public abstract class OilFluid extends ForgeFlowingFluid {
	public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(() -> RoboticaModFluidTypes.OIL_TYPE.get(), () -> RoboticaModFluids.OIL.get(), () -> RoboticaModFluids.FLOWING_OIL.get()).explosionResistance(100f)
			.tickRate(3).bucket(() -> RoboticaModItems.OIL_BUCKET.get()).block(() -> (LiquidBlock) RoboticaModBlocks.OIL.get());

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
