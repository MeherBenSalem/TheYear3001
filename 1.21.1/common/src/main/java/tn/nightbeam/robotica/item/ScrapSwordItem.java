package tn.nightbeam.robotica.item;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class ScrapSwordItem extends SwordItem {
	public ScrapSwordItem() {
		super(RoboticaTiers.SCRAP, new Item.Properties().attributes(SwordItem.createAttributes(RoboticaTiers.SCRAP, 3, -3F)));
	}
}
