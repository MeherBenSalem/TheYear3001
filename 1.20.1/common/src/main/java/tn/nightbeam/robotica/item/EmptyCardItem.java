package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class EmptyCardItem extends Item {
	public EmptyCardItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
	}
}