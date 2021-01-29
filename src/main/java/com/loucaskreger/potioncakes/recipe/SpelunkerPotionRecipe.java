package com.loucaskreger.potioncakes.recipe;

import com.loucaskreger.potioncakes.init.ModItems;
import com.loucaskreger.potioncakes.init.ModPotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class SpelunkerPotionRecipe implements IBrewingRecipe {

	@Override
	public boolean isInput(ItemStack input) {
		return PotionUtils.getPotionFromItem(input) == Potions.NIGHT_VISION
				|| PotionUtils.getPotionFromItem(input) == Potions.LONG_NIGHT_VISION;
	}

	@Override
	public boolean isIngredient(ItemStack ingredient) {
		return ingredient.getItem() == ModItems.MINERS_CATALYST.get();
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
		if (isInput(input) && isIngredient(ingredient)) {
			ItemStack stack = new ItemStack(Items.POTION);
			return PotionUtils.addPotionToItemStack(stack, ModPotionTypes.SPELUNKER_POTION.get());
		}
		return ItemStack.EMPTY;
	}

}
