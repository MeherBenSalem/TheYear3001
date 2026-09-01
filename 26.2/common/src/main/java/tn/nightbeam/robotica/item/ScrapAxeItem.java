package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class ScrapAxeItem extends Item {
	public ScrapAxeItem() {
		super(new Item.Properties().axe(RoboticaToolMaterials.SCRAP, 1f, -3.0f));
	}
}
