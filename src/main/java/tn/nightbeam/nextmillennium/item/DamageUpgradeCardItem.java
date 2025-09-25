
package tn.nightbeam.nextmillennium.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class DamageUpgradeCardItem extends Item {
	public DamageUpgradeCardItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.RARE));
	}
}
