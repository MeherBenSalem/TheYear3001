package tn.nightbeam.robotica.init;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import tn.nightbeam.robotica.RoboticaMod;

import java.util.List;
import java.util.Map;

public final class RoboticaArmorMaterials {
	public static final Holder<ArmorMaterial> SCRAP = register("scrap", Map.of(
			ArmorItem.Type.BOOTS, 2,
			ArmorItem.Type.LEGGINGS, 6,
			ArmorItem.Type.CHESTPLATE, 5,
			ArmorItem.Type.HELMET, 2), 9, SoundEvents.ARMOR_EQUIP_IRON, RoboticaModItems.SCRAP, 0f, 0f);
	public static final Holder<ArmorMaterial> TITANIUM = register("titanium", Map.of(
			ArmorItem.Type.BOOTS, 4,
			ArmorItem.Type.LEGGINGS, 8,
			ArmorItem.Type.CHESTPLATE, 7,
			ArmorItem.Type.HELMET, 3), 15, SoundEvents.ARMOR_EQUIP_DIAMOND, RoboticaModItems.TITANIUM_INGOT, 2f, 0.1f);
	public static final Holder<ArmorMaterial> COBALT = register("cobalt", Map.of(
			ArmorItem.Type.BOOTS, 4,
			ArmorItem.Type.LEGGINGS, 9,
			ArmorItem.Type.CHESTPLATE, 8,
			ArmorItem.Type.HELMET, 4), 20, SoundEvents.ARMOR_EQUIP_DIAMOND, RoboticaModItems.COBALT_INGOT, 3f, 0.15f);
	public static final Holder<ArmorMaterial> ORICHALCUM = register("orichalcum", Map.of(
			ArmorItem.Type.BOOTS, 4,
			ArmorItem.Type.LEGGINGS, 12,
			ArmorItem.Type.CHESTPLATE, 10,
			ArmorItem.Type.HELMET, 4), 25, SoundEvents.ARMOR_EQUIP_DIAMOND, RoboticaModItems.ORICHALCUM_INGOT, 4f, 0.2f);

	private RoboticaArmorMaterials() {
	}

	private static Holder<ArmorMaterial> register(String name, Map<ArmorItem.Type, Integer> defense, int enchantmentValue,
			Holder<SoundEvent> equipSound, java.util.function.Supplier<net.minecraft.world.item.Item> repairItem,
			float toughness, float knockbackResistance) {
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(RoboticaMod.MOD_ID, name);
		return Holder.direct(new ArmorMaterial(defense, enchantmentValue, equipSound,
				() -> Ingredient.of(repairItem.get()),
				List.of(new ArmorMaterial.Layer(id)), toughness, knockbackResistance));
	}
}
