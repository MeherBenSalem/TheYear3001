package tn.nightbeam.chronofall.block;

import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.core.FabricatorRecipes;
import tn.nightbeam.chronofall.item.FluxItem;
import tn.nightbeam.chronofall.item.RobotItem;
import tn.nightbeam.chronofall.menu.FabricatorMenu;

/**
 * The Fabricator: a recipe is selected in its screen, Fabricate takes the ingredients from the player's inventory, and
 * the item prints into the output slot over a few seconds. Hoppers can take the output from any side. The selected and
 * printing recipe are synced to clients for the hologram.
 */
public class FabricatorBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider {
    public static final int OUTPUT = 0;
    private static final int[] SLOTS = {OUTPUT};

    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private int selected;
    private int printing = -1;
    private int progress;
    private boolean blocked;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> selected;
                case 1 -> printing;
                case 2 -> progress;
                case 3 -> blocked ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public FabricatorBlockEntity(BlockPos pos, BlockState state) {
        super(ModContent.FABRICATOR_ENTITY.get(), pos, state);
    }

    public int selected() {
        return selected;
    }

    public int printing() {
        return printing;
    }

    public int progress() {
        return progress;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FabricatorBlockEntity fab) {
        FabricatorRecipes.Recipe recipe = FabricatorRecipes.get(fab.printing);
        if (recipe == null) {
            return;
        }
        if (fab.progress < recipe.ticks()) {
            fab.progress++;
            if (fab.progress % 20 == 1) {
                level.playSound(null, pos, ModContent.sound("fabricator_print"), SoundSource.BLOCKS, 0.6F, 0.9F + level.getRandom().nextFloat() * 0.2F);
            }
            if (fab.progress % 10 == 0) {
                fab.sync();
            }
            return;
        }
        ItemStack result = result(recipe);
        ItemStack out = fab.items.get(OUTPUT);
        if (out.isEmpty()) {
            fab.items.set(OUTPUT, result);
        } else if (ItemStack.isSameItemSameComponents(out, result) && out.getCount() + result.getCount() <= out.getMaxStackSize()) {
            out.grow(result.getCount());
        } else {
            if (!fab.blocked) {
                fab.blocked = true;
                fab.setChanged();
            }
            return;
        }
        fab.blocked = false;
        fab.printing = -1;
        fab.progress = 0;
        level.playSound(null, pos, ModContent.sound("fabricator_done"), SoundSource.BLOCKS, 0.9F, 1.0F);
        if (level instanceof ServerLevel server) {
            server.sendParticles(ModContent.FLUX_PARTICLE.get(), pos.getX() + 0.5, pos.getY() + 1.4, pos.getZ() + 0.5, 20, 0.25, 0.25, 0.25, 0.05);
        }
        fab.sync();
    }

    /** The printed stack; devices come out fully charged. */
    public static ItemStack result(FabricatorRecipes.Recipe recipe) {
        Item item = BuiltInRegistries.ITEM.getValue(Identifier.parse(recipe.result()));
        ItemStack stack = new ItemStack(item, recipe.count());
        if (item instanceof RobotItem robot) {
            return robot.charged();
        }
        if (item instanceof FluxItem flux) {
            return flux.charged(stack);
        }
        return stack;
    }

    /** A menu button: select recipe {@code index}. */
    public void select(int index) {
        if (FabricatorRecipes.get(index) != null && index != selected) {
            selected = index;
            sync();
        }
    }

    /** The Fabricate button: takes the ingredients from {@code player} and starts printing. */
    public boolean start(Player player) {
        FabricatorRecipes.Recipe recipe = FabricatorRecipes.get(selected);
        if (recipe == null || printing >= 0) {
            return false;
        }
        if (!player.getAbilities().instabuild) {
            if (!FabricatorRecipes.canAfford(recipe, counts(player.getInventory()))) {
                player.sendOverlayMessage(Component.translatable("message.chronofall.fabricator.missing"));
                return false;
            }
            for (Map.Entry<String, Integer> need : recipe.totals().entrySet()) {
                Item item = BuiltInRegistries.ITEM.getValue(Identifier.parse(need.getKey()));
                player.getInventory().clearOrCountMatchingItems(stack -> stack.is(item), false, need.getValue(), player.inventoryMenu.getCraftSlots());
            }
        }
        printing = selected;
        progress = 0;
        blocked = false;
        sync();
        return true;
    }

    /** How many of each item id the inventory holds. */
    public static Map<String, Integer> counts(Inventory inventory) {
        Map<String, Integer> counts = new HashMap<>();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                counts.merge(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString(), stack.getCount(), Integer::sum);
            }
        }
        return counts;
    }

    private void sync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.chronofall.fabricator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new FabricatorMenu(id, inventory, this, data);
    }

    // Saving and syncing --------------------------------------------------------------------------------------------

    @Override
    protected void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        items.set(OUTPUT, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items);
        selected = Math.max(0, tag.getIntOr("Selected", 0));
        printing = tag.getIntOr("Printing", -1);
        progress = tag.getIntOr("Progress", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, items);
        tag.putInt("Selected", selected);
        tag.putInt("Printing", printing);
        tag.putInt("Progress", progress);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Selected", selected);
        tag.putInt("Printing", printing);
        tag.putInt("Progress", progress);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // Container: one output slot that hoppers can empty --------------------------------------------------------------

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return items.get(OUTPUT).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack taken = ContainerHelper.removeItem(items, slot, amount);
        if (!taken.isEmpty()) {
            setChanged();
        }
        return taken;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return net.minecraft.world.Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return true;
    }
}
