package tn.nightbeam.robotica.item;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class OrichalcumAxeItem extends AxeItem {
	public OrichalcumAxeItem() {
		super(RoboticaTiers.ORICHALCUM, new Item.Properties().attributes(AxeItem.createAttributes(RoboticaTiers.ORICHALCUM, 1F, -2.8F)));
	}
}
