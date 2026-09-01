package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class ScrapPickaxeItem extends Item {
	public ScrapPickaxeItem() {
		super(new Item.Properties().pickaxe(RoboticaToolMaterials.SCRAP, 1f, -2.8f));
	}
}
