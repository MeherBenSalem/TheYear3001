package tn.nightbeam.chronofall.block;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Keeps natural Chrono Rifts from landing within {@code rifts.stabilizer_radius} blocks. It has no behaviour of its
 * own: its block entity only lets {@code RiftEvents} find stabilizers in loaded chunks without scanning blocks.
 */
public class RiftStabilizerBlock extends BaseEntityBlock {
    public static final MapCodec<RiftStabilizerBlock> CODEC = simpleCodec(RiftStabilizerBlock::new);

    public RiftStabilizerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RiftStabilizerBlockEntity(pos, state);
    }
}
