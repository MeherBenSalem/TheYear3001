package tn.nightbeam.robotica.item;

import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class OrichalcumPickaxeItem extends PickaxeItem {
	public OrichalcumPickaxeItem() {
		super(RoboticaTiers.ORICHALCUM, new Item.Properties().attributes(PickaxeItem.createAttributes(RoboticaTiers.ORICHALCUM, 1F, -2.7F)));
	}
}
