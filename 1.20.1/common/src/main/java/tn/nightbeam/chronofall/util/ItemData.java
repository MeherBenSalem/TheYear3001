package tn.nightbeam.chronofall.util;

import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

/** The mod's data on an item stack (flux, gate links, packed robots), kept in the stack's NBT tag. */
public final class ItemData {
    private ItemData() {
    }

    /** A copy of the stack's tag. */
    public static CompoundTag read(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null ? new CompoundTag() : tag.copy();
    }

    public static void write(ItemStack stack, CompoundTag tag) {
        stack.setTag(tag.isEmpty() ? null : tag);
    }

    public static void update(ItemStack stack, Consumer<CompoundTag> change) {
        CompoundTag tag = read(stack);
        change.accept(tag);
        write(stack, tag);
    }

    public static int getInt(ItemStack stack, String key) {
        CompoundTag tag = stack.getTag();
        return tag == null ? 0 : tag.getInt(key);
    }

    public static void putInt(ItemStack stack, String key, int value) {
        update(stack, tag -> tag.putInt(key, value));
    }
}
