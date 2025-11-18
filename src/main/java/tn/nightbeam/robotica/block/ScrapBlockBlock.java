package tn.nightbeam.robotica.block;

import org.checkerframework.checker.units.qual.s;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

public class ScrapBlockBlock extends Block {
	public ScrapBlockBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5f, 20f).lightLevel(s -> 8));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}
}