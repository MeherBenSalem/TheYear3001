package tn.nightbeam.robotica.item;

import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class CobaltShovelItem extends ShovelItem {
	public CobaltShovelItem() {
		super(RoboticaTiers.COBALT, new Item.Properties().attributes(ShovelItem.createAttributes(RoboticaTiers.COBALT, 1F, -2.8F)));
	}
}
