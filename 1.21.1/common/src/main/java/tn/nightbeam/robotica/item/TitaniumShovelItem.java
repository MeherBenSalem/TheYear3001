package tn.nightbeam.robotica.item;

import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class TitaniumShovelItem extends ShovelItem {
	public TitaniumShovelItem() {
		super(RoboticaTiers.TITANIUM, new Item.Properties().attributes(ShovelItem.createAttributes(RoboticaTiers.TITANIUM, 1F, -2.8F)));
	}
}
