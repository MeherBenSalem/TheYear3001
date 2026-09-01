package tn.nightbeam.robotica.item;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class TitaniumHoeItem extends HoeItem {
	public TitaniumHoeItem() {
		super(RoboticaTiers.TITANIUM, new Item.Properties().attributes(HoeItem.createAttributes(RoboticaTiers.TITANIUM, 0F, -0.5F)));
	}
}
