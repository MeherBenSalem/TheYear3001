package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class TitaniumPickaxeItem extends Item {
	public TitaniumPickaxeItem() {
		super(new Item.Properties().pickaxe(RoboticaToolMaterials.TITANIUM, 1f, -2.8f));
	}
}
