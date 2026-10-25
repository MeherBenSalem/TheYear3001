package tn.nightbeam.chronofall.content;

import java.util.EnumMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.level.block.Block;
import tn.nightbeam.chronofall.Chronofall;

/**
 * The four metal tiers and the jetpack's armour. Tier order, weakest first: scrap (between stone and iron), titanium (between iron
 * and diamond), cobalt (diamond), orichalcum (between diamond and netherite).
 */
public final class ModMaterials {
    public record Metal(ToolMaterial tool, ArmorMaterial armor, float axeDamage, float axeSpeed, float hoeDamage, float hoeSpeed) {
    }

    private static final Metal SCRAP = metal("scrap", 180, 5.0F, 1.5F, BlockTags.INCORRECT_FOR_STONE_TOOL, 12,
            new int[]{1, 3, 4, 2}, 0.0F, 0.0F, 10, 6.5F, -3.2F, -1.0F, -2.0F);
    private static final Metal TITANIUM = metal("titanium", 600, 7.0F, 2.5F, BlockTags.INCORRECT_FOR_IRON_TOOL, 16,
            new int[]{2, 6, 7, 3}, 1.0F, 0.0F, 22, 6.0F, -3.1F, -2.0F, -1.0F);
    private static final Metal COBALT = metal("cobalt", 1400, 8.0F, 3.0F, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 12,
            new int[]{3, 6, 8, 3}, 2.0F, 0.0F, 33, 5.0F, -3.0F, -3.0F, 0.0F);
    private static final Metal ORICHALCUM = metal("orichalcum", 1800, 8.5F, 3.5F, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 18,
            new int[]{3, 6, 8, 3}, 2.5F, 0.05F, 35, 5.0F, -3.0F, -4.0F, 0.0F);

    public static final ArmorMaterial JETPACK = armor("jetpack", new int[]{0, 0, 6, 0}, 1.0F, 0.0F, 16, 30, repairs("jetpack"));

    private ModMaterials() {
    }

    public static Metal metal(String name) {
        return switch (name) {
            case "scrap" -> SCRAP;
            case "titanium" -> TITANIUM;
            case "cobalt" -> COBALT;
            case "orichalcum" -> ORICHALCUM;
            default -> throw new IllegalArgumentException(name);
        };
    }

    private static TagKey<Item> repairs(String name) {
        return TagKey.create(Registries.ITEM, Chronofall.id("repairs_" + name));
    }

    /** {@code defense}: boots, leggings, chestplate, helmet. */
    private static Metal metal(String name, int uses, float speed, float damage, TagKey<Block> incorrect, int enchantment, int[] defense,
                               float toughness, float knockback, int durability, float axeDamage, float axeSpeed, float hoeDamage, float hoeSpeed) {
        TagKey<Item> repair = repairs(name + "_armor");
        ToolMaterial tool = new ToolMaterial(incorrect, uses, speed, damage, enchantment, repair);
        return new Metal(tool, armor(name, defense, toughness, knockback, enchantment, durability, repair), axeDamage, axeSpeed, hoeDamage, hoeSpeed);
    }

    private static ArmorMaterial armor(String name, int[] defense, float toughness, float knockback, int enchantment, int durability,
                                       TagKey<Item> repair) {
        EnumMap<ArmorType, Integer> map = new EnumMap<>(ArmorType.class);
        map.put(ArmorType.BOOTS, defense[0]);
        map.put(ArmorType.LEGGINGS, defense[1]);
        map.put(ArmorType.CHESTPLATE, defense[2]);
        map.put(ArmorType.HELMET, defense[3]);
        map.put(ArmorType.BODY, defense[2]);
        return new ArmorMaterial(durability, map, enchantment, SoundEvents.ARMOR_EQUIP_IRON, toughness, knockback, repair,
                ResourceKey.create(EquipmentAssets.ROOT_ID, Chronofall.id(name)));
    }
}
