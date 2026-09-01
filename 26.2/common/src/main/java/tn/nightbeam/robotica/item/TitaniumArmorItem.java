package tn.nightbeam.robotica.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;
import tn.nightbeam.robotica.init.RoboticaArmorMaterials;

public abstract class TitaniumArmorItem extends Item {
	protected TitaniumArmorItem(ArmorType type) {
		super(new Item.Properties().humanoidArmor(RoboticaArmorMaterials.TITANIUM, type));
	}

	public static class Helmet extends TitaniumArmorItem {
		public Helmet() { super(ArmorType.HELMET); }
	}

	public static class Chestplate extends TitaniumArmorItem {
		public Chestplate() { super(ArmorType.CHESTPLATE); }
	}

	public static class Leggings extends TitaniumArmorItem {
		public Leggings() { super(ArmorType.LEGGINGS); }
	}

	public static class Boots extends TitaniumArmorItem {
		public Boots() { super(ArmorType.BOOTS); }
	}
}
