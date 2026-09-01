package tn.nightbeam.robotica.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import tn.nightbeam.robotica.init.RoboticaArmorMaterials;

public abstract class OrichalcumArmorItem extends ArmorItem {
	public OrichalcumArmorItem(ArmorItem.Type type, Item.Properties properties) {
		super(RoboticaArmorMaterials.ORICHALCUM, type, properties);
	}

	public static class Helmet extends OrichalcumArmorItem {
		public Helmet() {
			super(ArmorItem.Type.HELMET, new Item.Properties());
		}
	}

	public static class Chestplate extends OrichalcumArmorItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties());
		}
	}

	public static class Leggings extends OrichalcumArmorItem {
		public Leggings() {
			super(ArmorItem.Type.LEGGINGS, new Item.Properties());
		}
	}

	public static class Boots extends OrichalcumArmorItem {
		public Boots() {
			super(ArmorItem.Type.BOOTS, new Item.Properties());
		}
	}
}
