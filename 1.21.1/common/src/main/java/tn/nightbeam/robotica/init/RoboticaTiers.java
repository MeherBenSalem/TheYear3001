package tn.nightbeam.robotica.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public final class RoboticaTiers {
	public static final Tier SCRAP = tier(256, 6f, 3f, 15, RoboticaModItems.SCRAP, BlockTags.INCORRECT_FOR_WOODEN_TOOL);
	public static final Tier TITANIUM = tier(2500, 10f, 9f, 18, RoboticaModItems.TITANIUM_INGOT, BlockTags.INCORRECT_FOR_IRON_TOOL);
	public static final Tier COBALT = tier(2800, 12f, 11f, 20, RoboticaModItems.COBALT_INGOT, BlockTags.INCORRECT_FOR_DIAMOND_TOOL);
	public static final Tier ORICHALCUM = tier(3500, 13f, 12f, 25, RoboticaModItems.ORICHALCUM_INGOT, BlockTags.INCORRECT_FOR_NETHERITE_TOOL);

	private RoboticaTiers() {
	}

	private static Tier tier(int uses, float speed, float attackDamageBonus, int enchantmentValue, Supplier<Item> repairItem, TagKey<Block> incorrectBlocks) {
		return new Tier() {
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
				return attackDamageBonus;
			}

			@Override
			public TagKey<Block> getIncorrectBlocksForDrops() {
				return incorrectBlocks;
			}

			@Override
			public int getEnchantmentValue() {
				return enchantmentValue;
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(repairItem.get()));
			}
		};
	}
}
