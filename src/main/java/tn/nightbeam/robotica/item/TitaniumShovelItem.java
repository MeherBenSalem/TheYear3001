
package tn.nightbeam.robotica.item;

import tn.nightbeam.robotica.init.RoboticaModItems;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class TitaniumShovelItem extends ShovelItem {
	public TitaniumShovelItem() {
		super(new Tier() {
			public int getUses() {
				return 2500;
			}

			public float getSpeed() {
				return 12f;
			}

			public float getAttackDamageBonus() {
				return 5f;
			}

			public int getLevel() {
				return 3;
			}

			public int getEnchantmentValue() {
				return 18;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(RoboticaModItems.TITANIUM_INGOT.get()));
			}
		}, 1, -2.8f, new Item.Properties());
	}
}
