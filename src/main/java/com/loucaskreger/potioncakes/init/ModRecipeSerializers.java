package com.loucaskreger.potioncakes.init;

import com.loucaskreger.potioncakes.PotionCakes;
import com.loucaskreger.potioncakes.recipe.CakeCookingRecipeSerializer;
import com.loucaskreger.potioncakes.recipe.ICookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeSerializers {

//	public static final IRecipeSerializer<CookingRecipe> COOKING_RECIPE_SERIALIZER = new CakeCookingRecipeSerializer();
	public static final IRecipeType<ICookingRecipe> COOKING_TYPE = registerType(ICookingRecipe.RECIPE_TYPE);

	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister
			.create(ForgeRegistries.RECIPE_SERIALIZERS, PotionCakes.MOD_ID);

	public static final RegistryObject<CakeCookingRecipeSerializer> COOKING_SERIALIZER = RECIPE_SERIALIZERS
			.register("cooking", () -> new CakeCookingRecipeSerializer());

	public static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getKey(this).toString();
		}
	}

	private static <T extends IRecipeType<?>> T registerType(ResourceLocation recipeType) {
		return Registry.register(Registry.RECIPE_TYPE, recipeType, (T) new RecipeType<>());

	}

}
