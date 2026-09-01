package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;
import tn.nightbeam.robotica.init.RoboticaArmorMaterials;

public abstract class OrichalcumArmorItem extends Item {
	protected OrichalcumArmorItem(ArmorType type) {
		super(new Item.Properties().humanoidArmor(RoboticaArmorMaterials.ORICHALCUM, type));
	}

	public static class Helmet extends OrichalcumArmorItem {
		public Helmet() { super(ArmorType.HELMET); }
	}

	public static class Chestplate extends OrichalcumArmorItem {
		public Chestplate() { super(ArmorType.CHESTPLATE); }
	}

	public static class Leggings extends OrichalcumArmorItem {
		public Leggings() { super(ArmorType.LEGGINGS); }
	}

	public static class Boots extends OrichalcumArmorItem {
		public Boots() { super(ArmorType.BOOTS); }
	}
}
