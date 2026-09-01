package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaToolMaterials;

public class OrichalcumPickaxeItem extends Item {
	public OrichalcumPickaxeItem() {
		super(new Item.Properties().pickaxe(RoboticaToolMaterials.ORICHALCUM, 1f, -2.8f));
	}
}
