package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class ScrapShovelItem extends Item {
	public ScrapShovelItem() {
		super(new Item.Properties().shovel(RoboticaToolMaterials.SCRAP, 1f, -3.0f));
	}
}
