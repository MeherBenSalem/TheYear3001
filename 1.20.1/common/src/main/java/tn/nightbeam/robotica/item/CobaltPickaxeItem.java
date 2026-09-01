package tn.nightbeam.robotica.item;

import tn.nightbeam.robotica.init.RoboticaModItems;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class CobaltPickaxeItem extends PickaxeItem {
	public CobaltPickaxeItem() {
		super(new Tier() {
			public int getUses() {
				return 2800;
			}

			public float getSpeed() {
				return 13f;
			}

			public float getAttackDamageBonus() {
				return 7f;
			}

			public int getLevel() {
				return 2;
			}

			public int getEnchantmentValue() {
				return 20;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(RoboticaModItems.COBALT_INGOT.get()));
			}
		}, 1, -2.7f, new Item.Properties());
	}
}