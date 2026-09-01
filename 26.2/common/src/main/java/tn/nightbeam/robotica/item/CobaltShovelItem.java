package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class CobaltShovelItem extends Item {
	public CobaltShovelItem() {
		super(new Item.Properties().shovel(RoboticaToolMaterials.COBALT, 1f, -3.0f));
	}
}
