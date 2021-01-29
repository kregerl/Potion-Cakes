package com.loucaskreger.potioncakes;

import com.loucaskreger.potioncakes.client.OvenTileEntityRenderer;
import com.loucaskreger.potioncakes.recipe.PotionCakeRecipe;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = PotionCakes.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EventSubscriber {


	@SubscribeEvent
	public static void onRegisterSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
		event.getRegistry().register(PotionCakeRecipe.SERIALIZER.setRegistryName(PotionCakes.MOD_ID, "cake_recipe"));
	}

	@SubscribeEvent
	public static void onStitch(TextureStitchEvent.Pre event) {
		if (!event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) {
			return;
		}

		event.addSprite(OvenTileEntityRenderer.TEXTURE);
	}

}
