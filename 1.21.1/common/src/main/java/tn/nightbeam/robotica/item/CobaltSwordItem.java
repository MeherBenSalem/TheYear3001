package tn.nightbeam.robotica.item;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class CobaltSwordItem extends SwordItem {
	public CobaltSwordItem() {
		super(RoboticaTiers.COBALT, new Item.Properties().attributes(SwordItem.createAttributes(RoboticaTiers.COBALT, 3, -2F)));
	}
}
