package tn.nightbeam.chronofall.content;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import tn.nightbeam.chronofall.Chronofall;

/**
 * The four metal tiers and the jetpack's armour. Tier order, weakest first: scrap (between stone and iron), titanium (between iron
 * and diamond), cobalt (diamond), orichalcum (between diamond and netherite).
 */
public final class ModMaterials {
    public record Metal(Tier tier, Holder<ArmorMaterial> armor, int durability, float axeDamage, float axeSpeed, float hoeDamage, float hoeSpeed) {
    }

    private static final Metal SCRAP = metal("scrap", 180, 5.0F, 1.5F, BlockTags.INCORRECT_FOR_STONE_TOOL, 12, "scrap",
            new int[]{1, 3, 4, 2}, 0.0F, 0.0F, 10, 6.5F, -3.2F, -1.0F, -2.0F);
    private static final Metal TITANIUM = metal("titanium", 600, 7.0F, 2.5F, BlockTags.INCORRECT_FOR_IRON_TOOL, 16, "titanium_ingot",
            new int[]{2, 6, 7, 3}, 1.0F, 0.0F, 22, 6.0F, -3.1F, -2.0F, -1.0F);
    private static final Metal COBALT = metal("cobalt", 1400, 8.0F, 3.0F, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 12, "cobalt_ingot",
            new int[]{3, 6, 8, 3}, 2.0F, 0.0F, 33, 5.0F, -3.0F, -3.0F, 0.0F);
    private static final Metal ORICHALCUM = metal("orichalcum", 1800, 8.5F, 3.5F, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 18, "orichalcum_ingot",
            new int[]{3, 6, 8, 3}, 2.5F, 0.05F, 35, 5.0F, -3.0F, -4.0F, 0.0F);

    public static final Holder<ArmorMaterial> JETPACK = armor("jetpack", new int[]{0, 0, 6, 0}, 1.0F, 0.0F, 16, "titanium_ingot");

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

    /** {@code defense}: boots, leggings, chestplate, helmet. */
    private static Metal metal(String name, int uses, float speed, float damage, TagKey<Block> incorrect, int enchantment, String repair,
                               int[] defense, float toughness, float knockback, int durability, float axeDamage, float axeSpeed,
                               float hoeDamage, float hoeSpeed) {
        Supplier<Ingredient> ingredient = () -> Ingredient.of(ModContent.item(repair));
        Tier tier = new Tier() {
            @Override
            public int getUses() {
                return uses;
            }

            @Override
            public float getSpeed() {
                return speed;
            }

            @Override
            public float getAttackDamageBonus() {
                return damage;
            }

            @Override
            public TagKey<Block> getIncorrectBlocksForDrops() {
                return incorrect;
            }

            @Override
            public int getEnchantmentValue() {
                return enchantment;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return ingredient.get();
            }
        };
        return new Metal(tier, armor(name, defense, toughness, knockback, enchantment, repair), durability, axeDamage, axeSpeed, hoeDamage, hoeSpeed);
    }

    private static Holder<ArmorMaterial> armor(String name, int[] defense, float toughness, float knockback, int enchantment, String repair) {
        EnumMap<ArmorItem.Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);
        map.put(ArmorItem.Type.BOOTS, defense[0]);
        map.put(ArmorItem.Type.LEGGINGS, defense[1]);
        map.put(ArmorItem.Type.CHESTPLATE, defense[2]);
        map.put(ArmorItem.Type.HELMET, defense[3]);
        map.put(ArmorItem.Type.BODY, defense[2]);
        return Holder.direct(new ArmorMaterial(map, enchantment, SoundEvents.ARMOR_EQUIP_IRON, () -> Ingredient.of(ModContent.item(repair)),
                List.of(new ArmorMaterial.Layer(Chronofall.id(name))), toughness, knockback));
    }
}
