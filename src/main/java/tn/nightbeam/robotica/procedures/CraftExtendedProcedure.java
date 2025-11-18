package tn.nightbeam.robotica.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

public class CraftExtendedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		ItemStack resultItem = ItemStack.EMPTY;
		if (CheckCircleScrapProcedure.execute(entity)) {
			if (RecipeMechProcedure.execute(entity)) {
				RemoveItemsFromCraftingGridProcedure.execute(entity);
			} else if (RecipeBoggieProcedure.execute(entity)) {
				RemoveItemsFromCraftingGridProcedure.execute(entity);
			} else if (RecipeSpaceShipProcedure.execute(entity)) {
				RemoveItemsFromCraftingGridProcedure.execute(entity);
			}
		}
	}
}