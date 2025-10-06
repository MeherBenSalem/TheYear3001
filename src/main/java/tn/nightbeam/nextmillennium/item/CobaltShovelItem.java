
package tn.nightbeam.nextmillennium.item;

import tn.nightbeam.nextmillennium.init.Theyear3001ModItems;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class CobaltShovelItem extends ShovelItem {
	public CobaltShovelItem() {
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
				return 3;
			}

			public int getEnchantmentValue() {
				return 20;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Theyear3001ModItems.COBALT_INGOT.get()));
			}
		}, 1, -2.8f, new Item.Properties());
	}
}
