package tn.nightbeam.robotica.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaArmorMaterials;

public abstract class CobaltArmorItem extends ArmorItem {
	public CobaltArmorItem(ArmorItem.Type type, Item.Properties properties) {
		super(RoboticaArmorMaterials.COBALT, type, properties);
	}

	public static class Helmet extends CobaltArmorItem {
		public Helmet() {
			super(ArmorItem.Type.HELMET, new Item.Properties());
		}
	}

	public static class Chestplate extends CobaltArmorItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties());
		}
	}

	public static class Leggings extends CobaltArmorItem {
		public Leggings() {
			super(ArmorItem.Type.LEGGINGS, new Item.Properties());
		}
	}

	public static class Boots extends CobaltArmorItem {
		public Boots() {
			super(ArmorItem.Type.BOOTS, new Item.Properties());
		}
	}
}
