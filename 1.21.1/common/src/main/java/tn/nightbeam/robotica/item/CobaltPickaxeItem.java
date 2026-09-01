package tn.nightbeam.robotica.item;

import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class CobaltPickaxeItem extends PickaxeItem {
	public CobaltPickaxeItem() {
		super(RoboticaTiers.COBALT, new Item.Properties().attributes(PickaxeItem.createAttributes(RoboticaTiers.COBALT, 1F, -2.7F)));
	}
}
