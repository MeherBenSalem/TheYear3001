package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class RawOrichalcumItem extends Item {
	public RawOrichalcumItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
	}
}