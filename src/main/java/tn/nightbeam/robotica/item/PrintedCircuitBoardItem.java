package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class PrintedCircuitBoardItem extends Item {
	public PrintedCircuitBoardItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON));
	}
}