package tn.nightbeam.chronofall.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import tn.nightbeam.chronofall.block.FabricatorBlockEntity;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.core.FabricatorRecipes;

/**
 * The Fabricator screen's server side: the output slot, the player's inventory and two kinds of button clicks
 * (vanilla's menu-button packet, so no custom networking): {@code 0..n-1} selects a recipe, {@link #FABRICATE} starts.
 */
public class FabricatorMenu extends AbstractContainerMenu {
    public static final int FABRICATE = 100;
    public static final int WIDTH = 256;
    public static final int HEIGHT = 222;
    public static final int OUTPUT_X = 216;
    public static final int OUTPUT_Y = 98;
    public static final int INVENTORY_X = 48;
    public static final int INVENTORY_Y = 140;

    private final Container container;
    private final ContainerData data;
    private final FabricatorBlockEntity fabricator;

    public FabricatorMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(1), new SimpleContainerData(4), null);
    }

    public FabricatorMenu(int id, Inventory inventory, FabricatorBlockEntity fabricator, ContainerData data) {
        this(id, inventory, fabricator, data, fabricator);
    }

    private FabricatorMenu(int id, Inventory inventory, Container container, ContainerData data, FabricatorBlockEntity fabricator) {
        super(ModContent.FABRICATOR_MENU.get(), id);
        this.container = container;
        this.data = data;
        this.fabricator = fabricator;
        addSlot(new Slot(container, FabricatorBlockEntity.OUTPUT, OUTPUT_X, OUTPUT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inventory, col + row * 9 + 9, INVENTORY_X + col * 18, INVENTORY_Y + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inventory, col, INVENTORY_X + col * 18, INVENTORY_Y + 58));
        }
        addDataSlots(data);
    }

    public int selected() {
        return data.get(0);
    }

    public int printing() {
        return data.get(1);
    }

    public int progress() {
        return data.get(2);
    }

    public boolean blocked() {
        return data.get(3) != 0;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (fabricator == null) {
            return false;
        }
        if (id == FABRICATE) {
            return fabricator.start(player);
        }
        if (FabricatorRecipes.get(id) != null) {
            fabricator.select(id);
            return true;
        }
        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (index != 0 || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        if (!moveItemStackTo(stack, 1, slots.size(), true)) {
            return ItemStack.EMPTY;
        }
        slot.setChanged();
        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }
}
