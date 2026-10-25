package tn.nightbeam.chronofall.item;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import tn.nightbeam.chronofall.core.Flux;
import tn.nightbeam.chronofall.util.ItemData;

/** An item that stores Flux: a charge bar, a tooltip line, and charge/drain helpers. */
public interface FluxItem {
    String KEY = "Flux";

    int capacity();

    default int flux(ItemStack stack) {
        return Math.max(0, Math.min(capacity(), ItemData.getInt(stack, KEY)));
    }

    default void setFlux(ItemStack stack, int value) {
        ItemData.putInt(stack, KEY, Math.max(0, Math.min(capacity(), value)));
    }

    /** Adds up to {@code amount}; returns how much was taken. */
    default int charge(ItemStack stack, int amount) {
        int room = Flux.room(flux(stack), amount, capacity());
        if (room > 0) {
            setFlux(stack, flux(stack) + room);
        }
        return room;
    }

    /** Takes {@code cost} if the stack holds it. */
    default boolean drain(ItemStack stack, int cost) {
        int stored = flux(stack);
        if (!Flux.canPay(stored, cost)) {
            return false;
        }
        setFlux(stack, stored - cost);
        return true;
    }

    default ItemStack charged(ItemStack stack) {
        setFlux(stack, capacity());
        return stack;
    }

    static void tooltip(ItemStack stack, FluxItem item, Consumer<Component> lines) {
        lines.accept(Component.translatable("tooltip.chronofall.flux", Flux.format(item.flux(stack), item.capacity()))
                .withStyle(ChatFormatting.AQUA));
    }

    static FluxItem of(ItemStack stack) {
        return stack.getItem() instanceof FluxItem flux ? flux : null;
    }
}
