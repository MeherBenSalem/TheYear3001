
package tn.nightbeam.nextmillennium.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;

import java.util.List;

public class BulkUpgradeCardItem extends Item {
	public BulkUpgradeCardItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.EPIC));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, level, list, flag);
		list.add(Component.translatable("item.theyear3001.bulk_upgrade_card.description_0"));
		list.add(Component.translatable("item.theyear3001.bulk_upgrade_card.description_1"));
	}
}
