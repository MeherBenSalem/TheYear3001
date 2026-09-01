package tn.nightbeam.robotica.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FlowingFluid;
import tn.nightbeam.robotica.init.RoboticaModBlocks;
import tn.nightbeam.robotica.init.RoboticaModFluids;
import tn.nightbeam.robotica.init.RoboticaModItems;

public abstract class OilFluid extends FlowingFluid {
	@Override
	public Fluid getFlowing() {
		return RoboticaModFluids.FLOWING_OIL.get();
	}

	@Override
	public Fluid getSource() {
		return RoboticaModFluids.OIL.get();
	}

	@Override
	protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
		Block.dropResources(state, level, pos, blockEntity);
	}

	@Override
	protected int getSlopeFindDistance(LevelReader level) {
		return 4;
	}

	@Override
	protected int getDropOff(LevelReader level) {
		return 1;
	}

	@Override
	public int getTickDelay(LevelReader level) {
		return 3;
	}

	@Override
	protected float getExplosionResistance() {
		return 100f;
	}

	@Override
	protected BlockState createLegacyBlock(FluidState state) {
		return RoboticaModBlocks.OIL.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
	}

	@Override
	public boolean isSame(Fluid fluid) {
		return fluid == RoboticaModFluids.OIL.get() || fluid == RoboticaModFluids.FLOWING_OIL.get();
	}

	@Override
	public Item getBucket() {
		return RoboticaModItems.OIL_BUCKET.get();
	}

	@Override
	public boolean canConvertToSource(Level level) {
		return false;
	}

	@Override
	public boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos, Fluid fluid, Direction direction) {
		return direction == Direction.DOWN && !fluid.isSame(this);
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
