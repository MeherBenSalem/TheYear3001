package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;
import tn.nightbeam.robotica.init.RoboticaArmorMaterials;

public abstract class CobaltArmorItem extends Item {
	protected CobaltArmorItem(ArmorType type) {
		super(new Item.Properties().humanoidArmor(RoboticaArmorMaterials.COBALT, type));
	}

	public static class Helmet extends CobaltArmorItem {
		public Helmet() { super(ArmorType.HELMET); }
	}

	public static class Chestplate extends CobaltArmorItem {
		public Chestplate() { super(ArmorType.CHESTPLATE); }
	}

	public static class Leggings extends CobaltArmorItem {
		public Leggings() { super(ArmorType.LEGGINGS); }
	}

	public static class Boots extends CobaltArmorItem {
		public Boots() { super(ArmorType.BOOTS); }
	}
}
