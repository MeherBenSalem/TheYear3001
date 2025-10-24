
package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class RawCobaltItem extends Item {
	public RawCobaltItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
	}
}
