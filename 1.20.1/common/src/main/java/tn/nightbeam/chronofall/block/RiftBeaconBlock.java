package tn.nightbeam.chronofall.block;

import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.core.ChronoConfig;

/** Takes Rift Shards on right-click and calls a Chrono Rift nearby after a countdown. */
public class RiftBeaconBlock extends BaseEntityBlock {

    public RiftBeaconBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RiftBeaconBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModContent.RIFT_BEACON_ENTITY.get(), RiftBeaconBlockEntity::serverTick);
    }

    @Nullable
    private InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                              BlockHitResult hit) {
        if (!stack.is(ModContent.item("rift_shard"))) {
            return null;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        int need = ChronoConfig.get().rifts().beaconShards();
        if (!(level.getBlockEntity(pos) instanceof RiftBeaconBlockEntity beacon) || beacon.active()) {
            return InteractionResult.CONSUME;
        }
        if (stack.getCount() < need && !player.getAbilities().instabuild) {
            player.displayClientMessage(Component.translatable("message.chronofall.beacon.need", need), true);
            return InteractionResult.CONSUME;
        }
        if (!player.getAbilities().instabuild) {
            stack.shrink(need);
        }
        beacon.start();
        player.displayClientMessage(Component.translatable("message.chronofall.beacon.started"), true);
        return InteractionResult.CONSUME;
    }

    private InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            player.displayClientMessage(Component.translatable("message.chronofall.beacon.need", ChronoConfig.get().rifts().beaconShards()), true);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, net.minecraft.world.InteractionHand hand, BlockHitResult hit) {
        InteractionResult item = useItemOn(player.getItemInHand(hand), state, level, pos, player, hand, hit);
        if (item != null) {
            return item;
        }
        return useWithoutItem(state, level, pos, player, hit);
    }
}
