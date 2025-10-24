
package tn.nightbeam.robotica.item;

import tn.nightbeam.robotica.init.RoboticaModFluids;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BucketItem;

public class OilItem extends BucketItem {
	public OilItem() {
		super(RoboticaModFluids.OIL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).rarity(Rarity.UNCOMMON));
	}
}
