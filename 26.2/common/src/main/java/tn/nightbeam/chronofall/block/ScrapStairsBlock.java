package tn.nightbeam.chronofall.block;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

/** Stairs with a public constructor (vanilla's is protected). */
public class ScrapStairsBlock extends StairBlock {
    public ScrapStairsBlock(BlockState base, Properties properties) {
        super(base, properties);
    }
}
