package tn.nightbeam.chronofall.item;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import tn.nightbeam.chronofall.core.Flux;

/** A hand-held device that runs on Flux and shows its charge as the item bar. */
public abstract class FluxToolItem extends Item implements FluxItem {
    private final int capacity;

    protected FluxToolItem(int capacity, Properties properties) {
        super(properties);
        this.capacity = capacity;
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Flux.barWidth(flux(stack), capacity());
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Flux.barColor(flux(stack), capacity());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> lines, TooltipFlag flag) {
        DescribedItem.describe(this, lines);
        FluxItem.tooltip(stack, this, lines);
    }
}
