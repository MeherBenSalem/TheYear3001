package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class ScrapItem extends Item {
	public ScrapItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
	}
}