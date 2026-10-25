package tn.nightbeam.chronofall.item;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import tn.nightbeam.chronofall.core.Flux;

/** Chest armour that flies while the wearer holds jump (see {@code Jetpack}). */
public class JetpackItem extends ArmorItem implements FluxItem {
    public JetpackItem(ArmorMaterial material, Properties properties) {
        super(material, Type.CHESTPLATE, properties);
    }

    @Override
    public int capacity() {
        return Flux.JETPACK_CAPACITY;
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
    public boolean isValidRepairItem(ItemStack stack, ItemStack repair) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> lines, TooltipFlag flag) {
        DescribedItem.describe(this, lines);
        FluxItem.tooltip(stack, this, lines);
    }
}
