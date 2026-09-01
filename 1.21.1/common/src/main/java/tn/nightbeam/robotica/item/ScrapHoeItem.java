package tn.nightbeam.robotica.item;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class ScrapHoeItem extends HoeItem {
	public ScrapHoeItem() {
		super(RoboticaTiers.SCRAP, new Item.Properties().attributes(HoeItem.createAttributes(RoboticaTiers.SCRAP, 0F, -3F)));
	}
}
