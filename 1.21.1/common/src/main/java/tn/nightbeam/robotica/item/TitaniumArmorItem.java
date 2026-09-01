package tn.nightbeam.robotica.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaArmorMaterials;

public abstract class TitaniumArmorItem extends ArmorItem {
	public TitaniumArmorItem(ArmorItem.Type type, Item.Properties properties) {
		super(RoboticaArmorMaterials.TITANIUM, type, properties);
	}

	public static class Helmet extends TitaniumArmorItem {
		public Helmet() {
			super(ArmorItem.Type.HELMET, new Item.Properties());
		}
	}

	public static class Chestplate extends TitaniumArmorItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties());
		}
	}

	public static class Leggings extends TitaniumArmorItem {
		public Leggings() {
			super(ArmorItem.Type.LEGGINGS, new Item.Properties());
		}
	}

	public static class Boots extends TitaniumArmorItem {
		public Boots() {
			super(ArmorItem.Type.BOOTS, new Item.Properties());
		}
	}
}
