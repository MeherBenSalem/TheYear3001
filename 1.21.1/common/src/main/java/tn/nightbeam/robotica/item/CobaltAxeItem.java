package tn.nightbeam.robotica.item;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class CobaltAxeItem extends AxeItem {
	public CobaltAxeItem() {
		super(RoboticaTiers.COBALT, new Item.Properties().attributes(AxeItem.createAttributes(RoboticaTiers.COBALT, 1F, -2.9F)));
	}
}
