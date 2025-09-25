
package tn.nightbeam.nextmillennium.jei_recipes;

import tn.nightbeam.nextmillennium.init.Theyear3001ModJeiPlugin;
import tn.nightbeam.nextmillennium.init.Theyear3001ModBlocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.constants.VanillaTypes;

public class ExtendedCraftingRecipeCategory implements IRecipeCategory<ExtendedCraftingRecipe> {
	public final static ResourceLocation UID = new ResourceLocation("theyear3001", "extended_crafting");
	public final static ResourceLocation TEXTURE = new ResourceLocation("theyear3001", "textures/screens/jei_render.png");
	private final IDrawable background;
	private final IDrawable icon;

	public ExtendedCraftingRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Theyear3001ModBlocks.CRAFTING_WORKSTATION.get().asItem()));
	}

	@Override
	public mezz.jei.api.recipe.RecipeType<ExtendedCraftingRecipe> getRecipeType() {
		return Theyear3001ModJeiPlugin.ExtendedCrafting_Type;
	}

	@Override
	public Component getTitle() {
		return Component.literal("Extended Crafting");
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ExtendedCraftingRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 7, 8).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.INPUT, 25, 8).addIngredients(recipe.getIngredients().get(1));
		builder.addSlot(RecipeIngredientRole.INPUT, 43, 8).addIngredients(recipe.getIngredients().get(2));
		builder.addSlot(RecipeIngredientRole.INPUT, 61, 8).addIngredients(recipe.getIngredients().get(3));
		builder.addSlot(RecipeIngredientRole.INPUT, 79, 8).addIngredients(recipe.getIngredients().get(4));
		builder.addSlot(RecipeIngredientRole.INPUT, 7, 26).addIngredients(recipe.getIngredients().get(5));
		builder.addSlot(RecipeIngredientRole.INPUT, 25, 26).addIngredients(recipe.getIngredients().get(6));
		builder.addSlot(RecipeIngredientRole.INPUT, 43, 26).addIngredients(recipe.getIngredients().get(7));
		builder.addSlot(RecipeIngredientRole.INPUT, 61, 26).addIngredients(recipe.getIngredients().get(8));
		builder.addSlot(RecipeIngredientRole.INPUT, 79, 26).addIngredients(recipe.getIngredients().get(9));
		builder.addSlot(RecipeIngredientRole.INPUT, 7, 44).addIngredients(recipe.getIngredients().get(10));
		builder.addSlot(RecipeIngredientRole.INPUT, 25, 44).addIngredients(recipe.getIngredients().get(11));
		builder.addSlot(RecipeIngredientRole.INPUT, 43, 44).addIngredients(recipe.getIngredients().get(12));
		builder.addSlot(RecipeIngredientRole.INPUT, 61, 44).addIngredients(recipe.getIngredients().get(13));
		builder.addSlot(RecipeIngredientRole.INPUT, 79, 44).addIngredients(recipe.getIngredients().get(14));
		builder.addSlot(RecipeIngredientRole.INPUT, 7, 62).addIngredients(recipe.getIngredients().get(15));
		builder.addSlot(RecipeIngredientRole.INPUT, 25, 62).addIngredients(recipe.getIngredients().get(16));
		builder.addSlot(RecipeIngredientRole.INPUT, 43, 62).addIngredients(recipe.getIngredients().get(17));
		builder.addSlot(RecipeIngredientRole.INPUT, 61, 62).addIngredients(recipe.getIngredients().get(18));
		builder.addSlot(RecipeIngredientRole.INPUT, 79, 62).addIngredients(recipe.getIngredients().get(19));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 134, 35).addItemStack(recipe.getResultItem(null));
	}
}
