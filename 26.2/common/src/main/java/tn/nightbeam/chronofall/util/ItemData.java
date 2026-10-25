package tn.nightbeam.chronofall.util;

import java.util.function.Consumer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/** The mod's data on an item stack (flux, gate links, packed robots), kept in the stack's custom data. */
public final class ItemData {
    private ItemData() {
    }

    /** A copy of the stack's custom data. */
    public static CompoundTag read(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? new CompoundTag() : data.copyTag();
    }

    public static void write(ItemStack stack, CompoundTag tag) {
        if (tag.isEmpty()) {
            stack.remove(DataComponents.CUSTOM_DATA);
        } else {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
    }

    public static void update(ItemStack stack, Consumer<CompoundTag> change) {
        CompoundTag tag = read(stack);
        change.accept(tag);
        write(stack, tag);
    }

    public static int getInt(ItemStack stack, String key) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? 0 : data.copyTag().getIntOr(key, 0);
    }

    public static void putInt(ItemStack stack, String key, int value) {
        update(stack, tag -> tag.putInt(key, value));
    }
}
