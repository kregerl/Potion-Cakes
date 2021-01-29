package com.loucaskreger.potioncakes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.loucaskreger.potioncakes.capability.CakeHolder;
import com.loucaskreger.potioncakes.capability.ICakeHolder;
import com.loucaskreger.potioncakes.client.OvenTileEntityRenderer;
import com.loucaskreger.potioncakes.client.gui.screen.OvenScreen;
import com.loucaskreger.potioncakes.init.ModBlocks;
import com.loucaskreger.potioncakes.init.ModContainerTypes;
import com.loucaskreger.potioncakes.init.ModEffects;
import com.loucaskreger.potioncakes.init.ModItems;
import com.loucaskreger.potioncakes.init.ModPotionTypes;
import com.loucaskreger.potioncakes.init.ModRecipeSerializers;
import com.loucaskreger.potioncakes.init.ModTileEntityTypes;
import com.loucaskreger.potioncakes.recipe.MilkBrewingRecipe;
import com.loucaskreger.potioncakes.recipe.SpelunkerPotionRecipe;
import com.loucaskreger.potioncakes.tileentity.OvenTileEntity;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PotionCakes.MOD_ID)
public class PotionCakes {

	public static final String MOD_ID = "potioncakes";
	public static final Logger LOGGER = LogManager.getLogger();

	public PotionCakes() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::setupCommon);
		bus.addListener(this::setupClient);
		ModBlocks.BLOCKS.register(bus);
		ModItems.ITEMS.register(bus);
		ModTileEntityTypes.TILE_ENTITIES.register(bus);
		ModContainerTypes.CONTAINER_TYPES.register(bus);
		ModEffects.EFFECTS.register(bus);
		ModPotionTypes.POTIONS.register(bus);
		ModRecipeSerializers.RECIPE_SERIALIZERS.register(bus);
	}

	private void setupCommon(final FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(ICakeHolder.class, new CakeHolder.Storage(), CakeHolder::new);
		BrewingRecipeRegistry.addRecipe(new MilkBrewingRecipe());
		BrewingRecipeRegistry.addRecipe(new SpelunkerPotionRecipe());
	}

	private void setupClient(final FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(ModBlocks.POTION_CAKE_BLOCK.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ModBlocks.OVEN_BLOCK.get(), RenderType.getCutoutMipped());
		ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.OVEN.get(), OvenTileEntityRenderer::new);

		ScreenManager.registerFactory(ModContainerTypes.OVEN_CONTAINER.get(), OvenScreen::new);

	}

	public static final ItemGroup CAKES_TAB = new ItemGroup("potionCakesTab") {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItems.INFUSED_MILK_ITEM.get());
		}

	};
}
