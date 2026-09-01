package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;
import tn.nightbeam.robotica.init.RoboticaArmorMaterials;

public abstract class ScrapArmorItem extends Item {
	protected ScrapArmorItem(ArmorType type) {
		super(new Item.Properties().humanoidArmor(RoboticaArmorMaterials.SCRAP, type));
	}

	public static class Helmet extends ScrapArmorItem {
		public Helmet() { super(ArmorType.HELMET); }
	}

	public static class Chestplate extends ScrapArmorItem {
		public Chestplate() { super(ArmorType.CHESTPLATE); }
	}

	public static class Leggings extends ScrapArmorItem {
		public Leggings() { super(ArmorType.LEGGINGS); }
	}

	public static class Boots extends ScrapArmorItem {
		public Boots() { super(ArmorType.BOOTS); }
	}
}
