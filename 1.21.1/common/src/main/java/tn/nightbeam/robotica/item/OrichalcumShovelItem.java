package tn.nightbeam.robotica.item;

import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class OrichalcumShovelItem extends ShovelItem {
	public OrichalcumShovelItem() {
		super(RoboticaTiers.ORICHALCUM, new Item.Properties().attributes(ShovelItem.createAttributes(RoboticaTiers.ORICHALCUM, 1F, -2.7F)));
	}
}
