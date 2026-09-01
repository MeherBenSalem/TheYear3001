package tn.nightbeam.robotica.item;

import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class ScrapPickaxeItem extends PickaxeItem {
	public ScrapPickaxeItem() {
		super(RoboticaTiers.SCRAP, new Item.Properties().attributes(PickaxeItem.createAttributes(RoboticaTiers.SCRAP, 1F, -3F)));
	}
}
