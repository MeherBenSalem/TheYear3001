package tn.nightbeam.robotica.item;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;


public class BulkUpgradeCardItem extends Item {
	public BulkUpgradeCardItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.EPIC));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, tooltipDisplay, tooltipAdder, flag);
		tooltipAdder.accept(Component.translatable("item.robotica.bulk_upgrade_card.description_0"));
	}
}