package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class CobaltPickaxeItem extends Item {
	public CobaltPickaxeItem() {
		super(new Item.Properties().pickaxe(RoboticaToolMaterials.COBALT, 1f, -2.8f));
	}
}
