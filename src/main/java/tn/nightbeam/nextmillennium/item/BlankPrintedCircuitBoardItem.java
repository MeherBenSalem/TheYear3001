
package tn.nightbeam.nextmillennium.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class BlankPrintedCircuitBoardItem extends Item {
	public BlankPrintedCircuitBoardItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON));
	}
}
