package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class ScrapHoeItem extends Item {
	public ScrapHoeItem() {
		super(new Item.Properties().hoe(RoboticaToolMaterials.SCRAP, 0f, -3.0f));
	}
}
