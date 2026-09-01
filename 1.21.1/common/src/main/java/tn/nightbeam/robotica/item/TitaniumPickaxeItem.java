package tn.nightbeam.robotica.item;

import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaTiers;

public class TitaniumPickaxeItem extends PickaxeItem {
	public TitaniumPickaxeItem() {
		super(RoboticaTiers.TITANIUM, new Item.Properties().attributes(PickaxeItem.createAttributes(RoboticaTiers.TITANIUM, 1F, -2.7F)));
	}
}
