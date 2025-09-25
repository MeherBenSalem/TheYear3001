
package tn.nightbeam.nextmillennium.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class RepairCardItem extends Item {
	public RepairCardItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.EPIC));
	}
}
