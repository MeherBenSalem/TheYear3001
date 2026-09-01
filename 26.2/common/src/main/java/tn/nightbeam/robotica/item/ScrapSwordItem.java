package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class ScrapSwordItem extends Item {
	public ScrapSwordItem() {
		super(new Item.Properties().sword(RoboticaToolMaterials.SCRAP, 3f, -3.0f));
	}
}
