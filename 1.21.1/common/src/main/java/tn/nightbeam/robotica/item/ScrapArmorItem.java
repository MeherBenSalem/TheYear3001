package tn.nightbeam.robotica.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaArmorMaterials;

public abstract class ScrapArmorItem extends ArmorItem {
	public ScrapArmorItem(ArmorItem.Type type, Item.Properties properties) {
		super(RoboticaArmorMaterials.SCRAP, type, properties);
	}

	public static class Helmet extends ScrapArmorItem {
		public Helmet() {
			super(ArmorItem.Type.HELMET, new Item.Properties());
		}
	}

	public static class Chestplate extends ScrapArmorItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties());
		}
	}

	public static class Leggings extends ScrapArmorItem {
		public Leggings() {
			super(ArmorItem.Type.LEGGINGS, new Item.Properties());
		}
	}

	public static class Boots extends ScrapArmorItem {
		public Boots() {
			super(ArmorItem.Type.BOOTS, new Item.Properties());
		}
	}
}
