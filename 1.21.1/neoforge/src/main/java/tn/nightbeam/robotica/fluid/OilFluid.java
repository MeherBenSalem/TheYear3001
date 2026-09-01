package tn.nightbeam.robotica.fluid;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import tn.nightbeam.robotica.init.RoboticaModBlocksForge;
import tn.nightbeam.robotica.init.RoboticaModFluidTypes;
import tn.nightbeam.robotica.init.RoboticaModFluidsForge;
import tn.nightbeam.robotica.init.RoboticaModItems;

public abstract class OilFluid extends BaseFlowingFluid {
	public static final BaseFlowingFluid.Properties PROPERTIES = new BaseFlowingFluid.Properties(() -> RoboticaModFluidTypes.OIL_TYPE.get(), () -> RoboticaModFluidsForge.OIL.get(), () -> RoboticaModFluidsForge.FLOWING_OIL.get()).explosionResistance(100f)
			.tickRate(3).bucket(() -> RoboticaModItems.OIL_BUCKET.get()).block(() -> (LiquidBlock) RoboticaModBlocksForge.OIL.get());

	private OilFluid() {
		super(PROPERTIES);
	}

	public static class Source extends OilFluid {
		@Override
		public int getAmount(FluidState state) {
			return 8;
		}

		@Override
		public boolean isSource(FluidState state) {
			return true;
		}
	}

	public static class Flowing extends OilFluid {
		@Override
		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

		@Override
		public int getAmount(FluidState state) {
			return state.getValue(LEVEL);
		}

		@Override
		public boolean isSource(FluidState state) {
			return false;
		}
	}
}
