package tn.nightbeam.chronofall.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;

/** An item with a grey description line ({@code <item key>.desc}) in its tooltip. */
public class DescribedItem extends Item {
    public DescribedItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> lines, TooltipFlag flag) {
        describe(this, lines);
    }

    public static void describe(Item item, List<Component> lines) {
        lines.add(Component.translatable(item.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
    }
}
