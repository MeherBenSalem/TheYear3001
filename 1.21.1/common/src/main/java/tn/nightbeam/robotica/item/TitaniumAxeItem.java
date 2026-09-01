package tn.nightbeam.robotica.item;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class TitaniumAxeItem extends AxeItem {
	public TitaniumAxeItem() {
		super(RoboticaTiers.TITANIUM, new Item.Properties().attributes(AxeItem.createAttributes(RoboticaTiers.TITANIUM, 1F, -2.9F)));
	}
}
