package com.loucaskreger.potioncakes.recipe;

import com.loucaskreger.potioncakes.PotionCakes;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public interface ICookingRecipe extends IRecipe<RecipeWrapper> {

	ResourceLocation RECIPE_TYPE = new ResourceLocation(PotionCakes.MOD_ID, "cooking");

	@Override
	default IRecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getValue(RECIPE_TYPE).get();
	}

	@Override
	default boolean canFit(int width, int height) {
		return false;
	}
	
	Ingredient getInput();
}
