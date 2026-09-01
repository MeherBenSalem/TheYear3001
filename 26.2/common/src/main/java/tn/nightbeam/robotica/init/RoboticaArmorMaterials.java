package tn.nightbeam.robotica.init;

import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import tn.nightbeam.robotica.RoboticaMod;

import java.util.Map;

public final class RoboticaArmorMaterials {
	public static final ArmorMaterial SCRAP = create("scrap_armor", 15, Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 5, ArmorType.LEGGINGS, 6, ArmorType.BOOTS, 2), 9, 0f, 0f, RoboticaToolMaterials.REPAIR_SCRAP);
	public static final ArmorMaterial TITANIUM = create("titanium_armor", 20, Map.of(ArmorType.HELMET, 3, ArmorType.CHESTPLATE, 7, ArmorType.LEGGINGS, 8, ArmorType.BOOTS, 4), 15, 2f, 0.1f, RoboticaToolMaterials.REPAIR_TITANIUM);
	public static final ArmorMaterial COBALT = create("cobalt_armor", 22, Map.of(ArmorType.HELMET, 4, ArmorType.CHESTPLATE, 8, ArmorType.LEGGINGS, 9, ArmorType.BOOTS, 5), 20, 3f, 0.1f, RoboticaToolMaterials.REPAIR_COBALT);
	public static final ArmorMaterial ORICHALCUM = create("orichalcum_armor", 30, Map.of(ArmorType.HELMET, 4, ArmorType.CHESTPLATE, 10, ArmorType.LEGGINGS, 12, ArmorType.BOOTS, 4), 25, 4f, 0.2f, RoboticaToolMaterials.REPAIR_ORICHALCUM);

	private RoboticaArmorMaterials() {
	}

	private static ArmorMaterial create(String assetName, int durability, Map<ArmorType, Integer> defense, int enchantmentValue, float toughness, float knockbackResistance, TagKey<Item> repair) {
		ResourceKey<EquipmentAsset> assetId = EquipmentAssets.createId(RoboticaMod.MOD_ID + "/" + assetName);
		return new ArmorMaterial(durability, defense, enchantmentValue, SoundEvents.ARMOR_EQUIP_IRON, toughness, knockbackResistance, repair, assetId);
	}
}
