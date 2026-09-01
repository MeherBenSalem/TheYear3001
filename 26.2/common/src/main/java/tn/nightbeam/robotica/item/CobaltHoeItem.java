package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class CobaltHoeItem extends Item {
	public CobaltHoeItem() {
		super(new Item.Properties().hoe(RoboticaToolMaterials.COBALT, 0f, -3.0f));
	}
}
