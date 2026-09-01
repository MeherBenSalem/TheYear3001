package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class OrichalcumShovelItem extends Item {
	public OrichalcumShovelItem() {
		super(new Item.Properties().shovel(RoboticaToolMaterials.ORICHALCUM, 1f, -3.0f));
	}
}
