package tn.nightbeam.robotica.item;

import tn.nightbeam.robotica.init.RoboticaModItems;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class OrichalcumShovelItem extends ShovelItem {
	public OrichalcumShovelItem() {
		super(new Tier() {
			public int getUses() {
				return 3500;
			}

			public float getSpeed() {
				return 14f;
			}

			public float getAttackDamageBonus() {
				return 8f;
			}

			public int getLevel() {
				return 3;
			}

			public int getEnchantmentValue() {
				return 25;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(RoboticaModItems.ORICHALCUM_INGOT.get()));
			}
		}, 1, -2.7f, new Item.Properties());
	}
}