package tn.nightbeam.robotica.item;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class OrichalcumSwordItem extends SwordItem {
	public OrichalcumSwordItem() {
		super(RoboticaTiers.ORICHALCUM, new Item.Properties().attributes(SwordItem.createAttributes(RoboticaTiers.ORICHALCUM, 3, -2F)));
	}
}
