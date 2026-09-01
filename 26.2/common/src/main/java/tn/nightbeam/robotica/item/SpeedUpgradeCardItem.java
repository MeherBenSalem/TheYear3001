package tn.nightbeam.robotica.item;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;


public class SpeedUpgradeCardItem extends Item {
	public SpeedUpgradeCardItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.RARE));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, tooltipDisplay, tooltipAdder, flag);
		tooltipAdder.accept(Component.translatable("item.robotica.speed_upgrade_card.description_0"));
	}
}