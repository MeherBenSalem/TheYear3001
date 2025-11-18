package tn.nightbeam.robotica.block;

import org.checkerframework.checker.units.qual.s;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Blocks;

public class ScrapStairsBlockBlock extends StairBlock {
	public ScrapStairsBlockBlock() {
		super(() -> Blocks.AIR.defaultBlockState(), BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5f, 20f).lightLevel(s -> 8));
	}

	@Override
	public float getExplosionResistance() {
		return 20f;
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return false;
	}
}