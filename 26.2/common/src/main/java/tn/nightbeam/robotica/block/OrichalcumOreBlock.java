package tn.nightbeam.robotica.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class OrichalcumOreBlock extends Block {
	public OrichalcumOreBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(5f, 20f).requiresCorrectToolForDrops());
	}

}