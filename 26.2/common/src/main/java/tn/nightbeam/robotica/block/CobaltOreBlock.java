package tn.nightbeam.robotica.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class CobaltOreBlock extends Block {
	public CobaltOreBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(5f, 20f).requiresCorrectToolForDrops());
	}

}