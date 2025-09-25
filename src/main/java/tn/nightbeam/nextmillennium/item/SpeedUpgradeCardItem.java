
package tn.nightbeam.nextmillennium.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class SpeedUpgradeCardItem extends Item {
	public SpeedUpgradeCardItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.RARE));
	}
}
