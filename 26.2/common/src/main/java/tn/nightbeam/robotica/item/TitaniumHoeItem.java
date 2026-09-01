package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class TitaniumHoeItem extends Item {
	public TitaniumHoeItem() {
		super(new Item.Properties().hoe(RoboticaToolMaterials.TITANIUM, 0f, -3.0f));
	}
}
