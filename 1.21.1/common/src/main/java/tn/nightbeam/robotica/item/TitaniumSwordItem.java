package tn.nightbeam.robotica.item;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class TitaniumSwordItem extends SwordItem {
	public TitaniumSwordItem() {
		super(RoboticaTiers.TITANIUM, new Item.Properties().attributes(SwordItem.createAttributes(RoboticaTiers.TITANIUM, 3, -2.2F)));
	}
}
