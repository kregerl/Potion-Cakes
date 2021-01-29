package com.loucaskreger.potioncakes.init;

import com.loucaskreger.potioncakes.PotionCakes;
import com.loucaskreger.potioncakes.block.OvenBlock;
import com.loucaskreger.potioncakes.block.PotionCakeBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			PotionCakes.MOD_ID);

	public static final RegistryObject<Block> POTION_CAKE_BLOCK = BLOCKS.register("potion_cake", PotionCakeBlock::new);
	public static final RegistryObject<Block> OVEN_BLOCK = BLOCKS.register("oven_block", OvenBlock::new);

}
