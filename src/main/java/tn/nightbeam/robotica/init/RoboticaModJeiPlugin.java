
package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.jei_recipes.ExtendedCraftingRecipeCategory;
import tn.nightbeam.robotica.jei_recipes.ExtendedCraftingRecipe;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.IModPlugin;

import java.util.Objects;
import java.util.List;

@JeiPlugin
public class RoboticaModJeiPlugin implements IModPlugin {
	public static mezz.jei.api.recipe.RecipeType<ExtendedCraftingRecipe> ExtendedCrafting_Type = new mezz.jei.api.recipe.RecipeType<>(ExtendedCraftingRecipeCategory.UID, ExtendedCraftingRecipe.class);

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation("robotica:jei_plugin");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new ExtendedCraftingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		List<ExtendedCraftingRecipe> ExtendedCraftingRecipes = recipeManager.getAllRecipesFor(ExtendedCraftingRecipe.Type.INSTANCE);
		registration.addRecipes(ExtendedCrafting_Type, ExtendedCraftingRecipes);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(RoboticaModBlocks.CRAFTING_WORKSTATION.get().asItem()), ExtendedCrafting_Type);
	}
}
