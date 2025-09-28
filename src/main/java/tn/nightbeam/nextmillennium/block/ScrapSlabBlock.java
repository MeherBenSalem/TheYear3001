
package tn.nightbeam.nextmillennium.block;

import org.checkerframework.checker.units.qual.s;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SlabBlock;

public class ScrapSlabBlock extends SlabBlock {
	public ScrapSlabBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5f, 20f).lightLevel(s -> 8));
	}
}
