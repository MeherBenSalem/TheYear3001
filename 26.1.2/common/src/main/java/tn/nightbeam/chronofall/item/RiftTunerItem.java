package tn.nightbeam.chronofall.item;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import tn.nightbeam.chronofall.block.RiftGateBlockEntity;
import tn.nightbeam.chronofall.core.ChronoConfig;
import tn.nightbeam.chronofall.util.ItemData;

/** Right-click one Rift Gate, then another: the two are linked both ways. */
public class RiftTunerItem extends net.minecraft.world.item.Item {
    public RiftTunerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level.getBlockEntity(context.getClickedPos()) instanceof RiftGateBlockEntity gate)) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        CompoundTag tag = ItemData.read(stack);
        BlockPos pos = context.getClickedPos();
        if (!tag.contains("Gate")) {
            CompoundTag first = new CompoundTag();
            first.putString("Dim", level.dimension().identifier().toString());
            first.putInt("X", pos.getX());
            first.putInt("Y", pos.getY());
            first.putInt("Z", pos.getZ());
            tag.put("Gate", first);
            ItemData.write(stack, tag);
            tell(player, Component.translatable("message.chronofall.tuner.stored", pos.getX(), pos.getY(), pos.getZ()));
            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 1.4F);
            return InteractionResult.CONSUME;
        }
        CompoundTag first = tag.getCompoundOrEmpty("Gate");
        ResourceKey<Level> firstDim = ResourceKey.create(Registries.DIMENSION, Identifier.parse(first.getStringOr("Dim", "minecraft:overworld")));
        BlockPos firstPos = new BlockPos(first.getIntOr("X", 0), first.getIntOr("Y", 0), first.getIntOr("Z", 0));
        tag.remove("Gate");
        ItemData.write(stack, tag);
        if (firstDim.equals(level.dimension()) && firstPos.equals(pos)) {
            tell(player, Component.translatable("message.chronofall.tuner.same"));
            return InteractionResult.CONSUME;
        }
        if (!firstDim.equals(level.dimension()) && !ChronoConfig.get().gates().crossDimension()) {
            tell(player, Component.translatable("message.chronofall.tuner.no_cross_dimension"));
            return InteractionResult.CONSUME;
        }
        ServerLevel firstLevel = level.getServer().getLevel(firstDim);
        if (firstLevel == null || !(firstLevel.getBlockEntity(firstPos) instanceof RiftGateBlockEntity other)) {
            tell(player, Component.translatable("message.chronofall.tuner.missing"));
            return InteractionResult.CONSUME;
        }
        gate.link(firstDim, firstPos);
        other.link(level.dimension(), pos);
        tell(player, Component.translatable("message.chronofall.tuner.linked", firstPos.getX(), firstPos.getY(), firstPos.getZ()));
        level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.5F);
        return InteractionResult.CONSUME;
    }

    private static void tell(Player player, Component message) {
        if (player != null) {
            player.sendOverlayMessage(message);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> lines, TooltipFlag flag) {
        DescribedItem.describe(this, lines);
        CompoundTag tag = ItemData.read(stack);
        if (tag.contains("Gate")) {
            CompoundTag gate = tag.getCompoundOrEmpty("Gate");
            lines.accept(Component.translatable("tooltip.chronofall.tuner.stored", gate.getIntOr("X", 0), gate.getIntOr("Y", 0), gate.getIntOr("Z", 0))
                    .withStyle(ChatFormatting.LIGHT_PURPLE));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return ItemData.read(stack).contains("Gate");
    }
}
