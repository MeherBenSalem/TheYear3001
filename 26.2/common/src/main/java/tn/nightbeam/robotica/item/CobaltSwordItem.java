package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class CobaltSwordItem extends Item {
	public CobaltSwordItem() {
		super(new Item.Properties().sword(RoboticaToolMaterials.COBALT, 3f, -2.0f));
	}
}
