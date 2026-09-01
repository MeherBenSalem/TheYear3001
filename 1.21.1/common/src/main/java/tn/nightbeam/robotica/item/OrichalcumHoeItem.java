package tn.nightbeam.robotica.item;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class OrichalcumHoeItem extends HoeItem {
	public OrichalcumHoeItem() {
		super(RoboticaTiers.ORICHALCUM, new Item.Properties().attributes(HoeItem.createAttributes(RoboticaTiers.ORICHALCUM, 0F, -0.5F)));
	}
}
