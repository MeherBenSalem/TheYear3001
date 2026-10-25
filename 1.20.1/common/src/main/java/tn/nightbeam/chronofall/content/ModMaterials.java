package tn.nightbeam.chronofall.content;

import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * The four metal tiers and the jetpack's armour. Tier order, weakest first: scrap (between stone and iron), titanium
 * (between iron and diamond), cobalt (diamond), orichalcum (between diamond and netherite).
 */
public final class ModMaterials {
    public record Metal(Tier tier, ArmorMaterial armor, float axeDamage, float axeSpeed, int hoeDamage, float hoeSpeed) {
    }

    /** Armour durability multipliers per piece, as vanilla: boots, leggings, chestplate, helmet. */
    private static final int[] DURABILITY = {13, 15, 16, 11};

    // Mining levels: 1 = stone, 2 = iron, 3 = diamond, 4 = netherite.
    private static final Metal SCRAP = metal("scrap", 180, 5.0F, 1.5F, 1, 12, "scrap",
            new int[]{1, 3, 4, 2}, 0.0F, 0.0F, 10, 6.5F, -3.2F, -1, -2.0F);
    private static final Metal TITANIUM = metal("titanium", 600, 7.0F, 2.5F, 2, 16, "titanium_ingot",
            new int[]{2, 6, 7, 3}, 1.0F, 0.0F, 22, 6.0F, -3.1F, -2, -1.0F);
    private static final Metal COBALT = metal("cobalt", 1400, 8.0F, 3.0F, 3, 12, "cobalt_ingot",
            new int[]{3, 6, 8, 3}, 2.0F, 0.0F, 33, 5.0F, -3.0F, -3, 0.0F);
    private static final Metal ORICHALCUM = metal("orichalcum", 1800, 8.5F, 3.5F, 4, 18, "orichalcum_ingot",
            new int[]{3, 6, 8, 3}, 2.5F, 0.05F, 35, 5.0F, -3.0F, -4, 0.0F);

    public static final ArmorMaterial JETPACK = armor("jetpack", new int[]{0, 0, 6, 0}, 1.0F, 0.0F, 16, "titanium_ingot", 30);

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
    private static Metal metal(String name, int uses, float speed, float damage, int level, int enchantment, String repair,
                               int[] defense, float toughness, float knockback, int durability, float axeDamage, float axeSpeed,
                               int hoeDamage, float hoeSpeed) {
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
            public int getLevel() {
                return level;
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
        return new Metal(tier, armor(name, defense, toughness, knockback, enchantment, repair, durability), axeDamage, axeSpeed, hoeDamage,
                hoeSpeed);
    }

    private static ArmorMaterial armor(String name, int[] defense, float toughness, float knockback, int enchantment, String repair,
                                       int durability) {
        String id = ModContent.NAMESPACE + ":" + name;
        return new ArmorMaterial() {
            @Override
            public int getDurabilityForType(ArmorItem.Type type) {
                return DURABILITY[slot(type)] * durability;
            }

            @Override
            public int getDefenseForType(ArmorItem.Type type) {
                return defense[slot(type)];
            }

            @Override
            public int getEnchantmentValue() {
                return enchantment;
            }

            @Override
            public SoundEvent getEquipSound() {
                return SoundEvents.ARMOR_EQUIP_IRON;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return Ingredient.of(ModContent.item(repair));
            }

            @Override
            public String getName() {
                return id;
            }

            @Override
            public float getToughness() {
                return toughness;
            }

            @Override
            public float getKnockbackResistance() {
                return knockback;
            }
        };
    }

    private static int slot(ArmorItem.Type type) {
        return switch (type) {
            case BOOTS -> 0;
            case LEGGINGS -> 1;
            case CHESTPLATE -> 2;
            case HELMET -> 3;
        };
    }
}
