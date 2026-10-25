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
import tn.nightbeam.chronofall.Chronofall;
import tn.nightbeam.chronofall.block.FluxChargerBlockEntity;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.item.FluxItem;

/** The Flux Charger screen's server side: a fuel slot, a charge slot and the player's inventory. */
public class FluxChargerMenu extends AbstractContainerMenu {
    public static final int FUEL_X = 44;
    public static final int CHARGE_X = 116;
    public static final int SLOT_Y = 35;

    private final Container container;
    private final ContainerData data;
    private final net.minecraft.world.level.Level level;

    public FluxChargerMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(2), new SimpleContainerData(3));
    }

    public FluxChargerMenu(int id, Inventory inventory, Container container, ContainerData data) {
        super(ModContent.FLUX_CHARGER_MENU.get(), id);
        this.container = container;
        this.data = data;
        this.level = inventory.player.level();
        addSlot(new Slot(container, FluxChargerBlockEntity.FUEL, FUEL_X, SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isFuel(stack);
            }
        });
        addSlot(new Slot(container, FluxChargerBlockEntity.CHARGE, CHARGE_X, SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return FluxItem.of(stack) != null;
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inventory, col, 8 + col * 18, 142));
        }
        addDataSlots(data);
    }

    boolean isFuel(ItemStack stack) {
        return FluxChargerBlockEntity.isFuel(level, stack);
    }

    /** Stored Flux (the data slot carries it in tens to fit a short). */
    public int stored() {
        return data.get(0) * 10;
    }

    /** Fuel left in the current burn, 0 to 100. */
    public int burn() {
        return data.get(1);
    }

    public boolean charging() {
        return data.get(2) != 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        if (index < 2) {
            if (!moveItemStackTo(stack, 2, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (FluxItem.of(stack) != null) {
            if (!moveItemStackTo(stack, 1, 2, false)) {
                return ItemStack.EMPTY;
            }
        } else if (isFuel(stack)) {
            if (!moveItemStackTo(stack, 0, 1, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }
}
