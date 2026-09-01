package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class CobaltAxeItem extends Item {
	public CobaltAxeItem() {
		super(new Item.Properties().axe(RoboticaToolMaterials.COBALT, 1f, -3.0f));
	}
}
