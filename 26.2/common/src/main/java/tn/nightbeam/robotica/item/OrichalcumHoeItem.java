package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class OrichalcumHoeItem extends Item {
	public OrichalcumHoeItem() {
		super(new Item.Properties().hoe(RoboticaToolMaterials.ORICHALCUM, 0f, -3.0f));
	}
}
