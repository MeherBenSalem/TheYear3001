package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class OrichalcumAxeItem extends Item {
	public OrichalcumAxeItem() {
		super(new Item.Properties().axe(RoboticaToolMaterials.ORICHALCUM, 1f, -3.0f));
	}
}
