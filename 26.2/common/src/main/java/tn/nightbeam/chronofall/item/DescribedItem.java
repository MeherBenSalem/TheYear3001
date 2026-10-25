package tn.nightbeam.chronofall.item;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

/** An item with a grey description line ({@code <item key>.desc}) in its tooltip. */
public class DescribedItem extends Item {
    public DescribedItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> lines, TooltipFlag flag) {
        describe(this, lines);
    }

    public static void describe(Item item, Consumer<Component> lines) {
        lines.accept(Component.translatable(item.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
    }
}
