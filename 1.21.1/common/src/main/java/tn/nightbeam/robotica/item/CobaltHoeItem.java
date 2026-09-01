package tn.nightbeam.robotica.item;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class CobaltHoeItem extends HoeItem {
	public CobaltHoeItem() {
		super(RoboticaTiers.COBALT, new Item.Properties().attributes(HoeItem.createAttributes(RoboticaTiers.COBALT, 0F, -0.5F)));
	}
}
