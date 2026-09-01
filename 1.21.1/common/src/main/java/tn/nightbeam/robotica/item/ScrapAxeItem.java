package tn.nightbeam.robotica.item;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class ScrapAxeItem extends AxeItem {
	public ScrapAxeItem() {
		super(RoboticaTiers.SCRAP, new Item.Properties().attributes(AxeItem.createAttributes(RoboticaTiers.SCRAP, 1F, -3F)));
	}
}
