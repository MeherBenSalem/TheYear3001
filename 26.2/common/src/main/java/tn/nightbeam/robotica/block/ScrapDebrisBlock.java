package tn.nightbeam.robotica.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class ScrapDebrisBlock extends Block {
	public ScrapDebrisBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(7f, 30f).requiresCorrectToolForDrops());
	}

}