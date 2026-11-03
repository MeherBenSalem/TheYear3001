package tn.nightbeam.chronofall.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import tn.nightbeam.chronofall.content.ModContent;

/** Marks a Rift Stabilizer so rifts can find it through the chunk's block entities. No ticking, no data. */
public class RiftStabilizerBlockEntity extends BlockEntity {
    public RiftStabilizerBlockEntity(BlockPos pos, BlockState state) {
        super(ModContent.RIFT_STABILIZER_ENTITY.get(), pos, state);
    }
}
