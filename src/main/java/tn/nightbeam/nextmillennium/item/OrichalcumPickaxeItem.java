
package tn.nightbeam.nextmillennium.item;

import tn.nightbeam.nextmillennium.init.Theyear3001ModItems;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class OrichalcumPickaxeItem extends PickaxeItem {
	public OrichalcumPickaxeItem() {
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
				return 2;
			}

			public int getEnchantmentValue() {
				return 25;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Theyear3001ModItems.ORICHALCUM_INGOT.get()));
			}
		}, 1, -2.7f, new Item.Properties());
	}
}
