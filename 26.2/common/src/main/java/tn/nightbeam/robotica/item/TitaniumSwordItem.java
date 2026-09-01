package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class TitaniumSwordItem extends Item {
	public TitaniumSwordItem() {
		super(new Item.Properties().sword(RoboticaToolMaterials.TITANIUM, 3f, -2.2f));
	}
}
