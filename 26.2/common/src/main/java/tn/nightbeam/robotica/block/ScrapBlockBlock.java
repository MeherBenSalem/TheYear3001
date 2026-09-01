package tn.nightbeam.robotica.block;


import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class ScrapBlockBlock extends Block {
	public ScrapBlockBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5f, 20f).lightLevel(s -> 8));
	}

}