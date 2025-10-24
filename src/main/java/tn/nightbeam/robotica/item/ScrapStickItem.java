
package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class ScrapStickItem extends Item {
	public ScrapStickItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
	}
}
