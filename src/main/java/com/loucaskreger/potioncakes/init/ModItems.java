package com.loucaskreger.potioncakes.init;

import com.loucaskreger.potioncakes.PotionCakes;
import com.loucaskreger.potioncakes.item.CakeBoxItem;
import com.loucaskreger.potioncakes.item.InfusedMilkItem;
import com.loucaskreger.potioncakes.item.ModBlockItem;
import com.loucaskreger.potioncakes.item.ModItem;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
			PotionCakes.MOD_ID);

	public static final RegistryObject<Item> INFUSED_MILK_ITEM = ITEMS.register("infused_milk", InfusedMilkItem::new);

	public static final RegistryObject<Item> CAKE_BOX = ITEMS.register("cake_box", CakeBoxItem::new);

	public static final RegistryObject<Item> POTION_CAKE_BLOCK = ITEMS.register("potion_cake",
			() -> new ModBlockItem(ModBlocks.POTION_CAKE_BLOCK.get()));
	
	public static final RegistryObject<Item> OVEN_BLOCK = ITEMS.register("oven",
			() -> new ModBlockItem(ModBlocks.OVEN_BLOCK.get()));
	
	public static final RegistryObject<Item> MINERS_CATALYST = ITEMS.register("miners_catalyst",() -> new ModItem());

}
