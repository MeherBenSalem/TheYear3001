package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class TitaniumAxeItem extends Item {
	public TitaniumAxeItem() {
		super(new Item.Properties().axe(RoboticaToolMaterials.TITANIUM, 1f, -3.0f));
	}
}
