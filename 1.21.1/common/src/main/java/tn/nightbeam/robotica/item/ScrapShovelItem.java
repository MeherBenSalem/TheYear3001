package tn.nightbeam.robotica.item;

import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class ScrapShovelItem extends ShovelItem {
	public ScrapShovelItem() {
		super(RoboticaTiers.SCRAP, new Item.Properties().attributes(ShovelItem.createAttributes(RoboticaTiers.SCRAP, 1F, -3F)));
	}
}
