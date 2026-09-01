package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class TitaniumShovelItem extends Item {
	public TitaniumShovelItem() {
		super(new Item.Properties().shovel(RoboticaToolMaterials.TITANIUM, 1f, -3.0f));
	}
}
