package com.loucaskreger.potioncakes.recipe;

import java.util.List;
import com.google.gson.JsonObject;
import com.loucaskreger.potioncakes.PotionCakes;
import com.loucaskreger.potioncakes.block.PotionCakeBlock;
import com.loucaskreger.potioncakes.init.ModBlocks;
import com.loucaskreger.potioncakes.item.InfusedMilkItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class PotionCakeRecipe extends SpecialRecipe {

	public static final PotionCakeSerializer SERIALIZER = new PotionCakeSerializer();
	public static final Item[] recipeBase = new Item[] { null, null, null, Items.SUGAR, Items.EGG, Items.SUGAR,
			Items.WHEAT, Items.WHEAT, Items.WHEAT };

	public PotionCakeRecipe(ResourceLocation idIn) {
		super(idIn);
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		int hasMilk = 0;
		int hasBase = 0;
		for (int i = 0; i < 3; ++i) {
			if (inv.getStackInSlot(i).getItem() instanceof InfusedMilkItem) {
				++hasMilk;
			}
		}
		for (int i = 3; i < 9; ++i) {
			if (recipeBase[i] != null) {
				if (inv.getStackInSlot(i).getItem() == recipeBase[i]) {
					++hasBase;
				}
			}
		}
		System.out.println(hasBase);
		System.out.println(hasMilk);
		return hasBase == 6 && hasMilk == 3;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		ItemStack stack0 = inv.getStackInSlot(0);
		ItemStack stack1 = inv.getStackInSlot(1);
		ItemStack stack2 = inv.getStackInSlot(2);
		List<EffectInstance> effects = PotionUtils.getEffectsFromStack(stack0);
		if (areEffectsSame(stack0, stack1, stack2)) {
			if (!effects.isEmpty() && effects.size() == 1) {
				PotionCakeBlock block = (PotionCakeBlock) ModBlocks.POTION_CAKE_BLOCK.get();
				ItemStack stack = new ItemStack(block);
				return PotionUtils.appendEffects(stack.copy(), effects);
			}
		}
		return ItemStack.EMPTY;
	}

	private boolean areEffectsSame(ItemStack... stacks) {
		int correctCount = 0;
		CompoundNBT tag = stacks[0].getTag();
		for (ItemStack s : stacks) {
			if (s.getTag().equals(tag)) {
				++correctCount;
			}

		}
		return correctCount >= 3;

	}

	@Override
	public boolean canFit(int width, int height) {
		return width >= 3 && height >= 3;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class PotionCakeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<PotionCakeRecipe> {

		@Override
		public PotionCakeRecipe read(ResourceLocation recipeId, JsonObject json) {
			return new PotionCakeRecipe(recipeId);
		}

		@Override
		public PotionCakeRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			return new PotionCakeRecipe(recipeId);
		}

		@Override
		public void write(PacketBuffer buffer, PotionCakeRecipe recipe) {
			// buffer.write() if needed, must match same order as read above.
		}

	}

}
