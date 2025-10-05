
package tn.nightbeam.nextmillennium.item;

import tn.nightbeam.nextmillennium.init.Theyear3001ModItems;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.HoeItem;

public class TitaniumHoeItem extends HoeItem {
	public TitaniumHoeItem() {
		super(new Tier() {
			public int getUses() {
				return 2500;
			}

			public float getSpeed() {
				return 14f;
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
				return Ingredient.of(new ItemStack(Theyear3001ModItems.SCRAP.get()));
			}
		}, 0, -0.5f, new Item.Properties());
	}
}
