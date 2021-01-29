package com.loucaskreger.potioncakes.recipe;

import com.loucaskreger.potioncakes.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class MilkBrewingRecipe implements IBrewingRecipe {

	@Override
	public boolean isInput(ItemStack input) {
		return input.getItem() == Items.MILK_BUCKET || input.getItem() == ModItems.INFUSED_MILK_ITEM.get();
	}

	@Override
	public boolean isIngredient(ItemStack ingredient) {
		return PotionBrewing.isReagent(ingredient);
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
		if (!input.isEmpty() && !ingredient.isEmpty() && isIngredient(ingredient)) {
			ItemStack result = null;
			if (input.getItem() == Items.MILK_BUCKET) {
				result = PotionBrewing.doReaction(ingredient, Items.POTION.getDefaultInstance());
			} else if (input.getItem() == ModItems.INFUSED_MILK_ITEM.get()) {
				ItemStack potionStack = Items.POTION.getDefaultInstance();
				Potion potion = PotionUtils.getPotionFromItem(input);
				if (potion != null) {
					PotionUtils.addPotionToItemStack(potionStack, potion);
					result = PotionBrewing.doReaction(ingredient, potionStack);
				}
			}
			if (result != null && result != input) {
				if (PotionUtils.getPotionFromItem(input) != PotionUtils.getPotionFromItem(result)) {
					Potion resultPot = PotionUtils.getPotionFromItem(result);
					return PotionUtils.addPotionToItemStack(new ItemStack(ModItems.INFUSED_MILK_ITEM.get()), resultPot);
				}
			}
		}

		return ItemStack.EMPTY;
	}
}