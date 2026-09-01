package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class OrichalcumSwordItem extends Item {
	public OrichalcumSwordItem() {
		super(new Item.Properties().sword(RoboticaToolMaterials.ORICHALCUM, 3f, -2.0f));
	}
}
