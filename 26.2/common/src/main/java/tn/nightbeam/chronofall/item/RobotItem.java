package tn.nightbeam.chronofall.item;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import tn.nightbeam.chronofall.entity.Packable;
import tn.nightbeam.chronofall.util.ItemData;

/**
 * A packed Drone, Mech or Skiff. Using it on a block deploys the robot with the charge, chips and upgrades it was
 * packed with; sneak-right-clicking the robot packs it again.
 */
public class RobotItem extends FluxToolItem {
    private final Supplier<? extends EntityType<?>> type;

    public RobotItem(Supplier<? extends EntityType<?>> type, int capacity, Properties properties) {
        super(capacity, properties);
        this.type = type;
    }

    /** A freshly built robot: full charge (the creative tab and the Fabricator hand these out). */
    public ItemStack charged() {
        return charged(new ItemStack(this));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel server)) {
            return InteractionResult.SUCCESS;
        }
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        Entity entity = type.get().create(server, EntitySpawnReason.SPAWN_ITEM_USE);
        if (entity == null) {
            return InteractionResult.FAIL;
        }
        Player player = context.getPlayer();
        float yaw = player == null ? 0 : player.getYRot();
        Vec3 at = Vec3.atBottomCenterOf(pos);
        entity.snapTo(at.x, at.y, at.z, yaw, 0);
        if (!server.noCollision(entity)) {
            if (player != null) {
                player.sendOverlayMessage(Component.translatable("message.chronofall.robot.no_room"));
            }
            return InteractionResult.FAIL;
        }
        ItemStack stack = context.getItemInHand();
        CompoundTag tag = ItemData.read(stack);
        if (!tag.contains(FluxItem.KEY)) {
            tag.putInt(FluxItem.KEY, 0);
        }
        if (entity instanceof Packable packable) {
            packable.unpack(tag, player);
        }
        server.addFreshEntity(entity);
        server.playSound(null, pos, SoundEvents.IRON_GOLEM_REPAIR, SoundSource.NEUTRAL, 0.8F, 1.3F);
        if (player == null || !player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResult.CONSUME;
    }

    /** The item for a robot that was just packed: its data goes on the stack. */
    public static ItemStack packed(RobotItem item, CompoundTag data) {
        ItemStack stack = new ItemStack(item);
        ItemData.write(stack, data);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> lines, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, lines, flag);
        CompoundTag tag = ItemData.read(stack);
        for (String chip : List.of("collector_chip", "guard_chip", "lamp_chip")) {
            if (tag.getBooleanOr(chip, false)) {
                lines.accept(Component.translatable("item.chronofall." + chip).withStyle(ChatFormatting.DARK_AQUA));
            }
        }
        CompoundTag upgrades = tag.getCompoundOrEmpty("Upgrades");
        for (String key : upgrades.keySet()) {
            lines.accept(Component.translatable("tooltip.chronofall.upgrade." + key, upgrades.getIntOr(key, 0)).withStyle(ChatFormatting.DARK_AQUA));
        }
    }

}
