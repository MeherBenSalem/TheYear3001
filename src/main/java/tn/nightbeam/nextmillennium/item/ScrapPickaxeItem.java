
package tn.nightbeam.nextmillennium.item;

import tn.nightbeam.nextmillennium.init.Theyear3001ModItems;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class ScrapPickaxeItem extends PickaxeItem {
	public ScrapPickaxeItem() {
		super(new Tier() {
			public int getUses() {
				return 256;
			}

			public float getSpeed() {
				return 6f;
			}

			public float getAttackDamageBonus() {
				return 2f;
			}

			public int getLevel() {
				return 2;
			}

			public int getEnchantmentValue() {
				return 15;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Theyear3001ModItems.SCRAP.get()));
			}
		}, 1, -3f, new Item.Properties());
	}
}
