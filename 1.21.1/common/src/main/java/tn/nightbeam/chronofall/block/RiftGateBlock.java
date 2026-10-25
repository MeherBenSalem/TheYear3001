package tn.nightbeam.chronofall.block;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.item.RiftTunerItem;

/** A teleport pad: stand on a linked gate to arrive on its partner (link two with a Rift Tuner). */
public class RiftGateBlock extends BaseEntityBlock {
    public static final MapCodec<RiftGateBlock> CODEC = simpleCodec(RiftGateBlock::new);
    public static final BooleanProperty LINKED = BooleanProperty.create("linked");
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 4, 16);

    public RiftGateBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(LINKED, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LINKED);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RiftGateBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModContent.RIFT_GATE_ENTITY.get(), RiftGateBlockEntity::serverTick);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                              BlockHitResult hit) {
        // Let the Rift Tuner's own use run instead of the status message.
        return stack.getItem() instanceof RiftTunerItem ? ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION
                : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof RiftGateBlockEntity gate) {
            player.displayClientMessage(gate.describe(), true);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LINKED)) {
            return;
        }
        for (int i = 0; i < 2; i++) {
            double a = random.nextDouble() * Math.PI * 2;
            double r = 0.3 + random.nextDouble() * 0.15;
            level.addParticle(ModContent.RIFT_PARTICLE.get(), pos.getX() + 0.5 + Math.cos(a) * r, pos.getY() + 0.3, pos.getZ() + 0.5 + Math.sin(a) * r,
                    0, 0.06 + random.nextDouble() * 0.06, 0);
        }
    }
}
