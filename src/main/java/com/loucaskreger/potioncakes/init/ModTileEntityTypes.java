package com.loucaskreger.potioncakes.init;

import com.loucaskreger.potioncakes.PotionCakes;
import com.loucaskreger.potioncakes.tileentity.OvenTileEntity;
import com.loucaskreger.potioncakes.tileentity.PotionCakeTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntityTypes {

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister
			.create(ForgeRegistries.TILE_ENTITIES, PotionCakes.MOD_ID);

	public static final RegistryObject<TileEntityType<PotionCakeTileEntity>> POTION_CAKE = TILE_ENTITIES
			.register("potion_cake", () -> TileEntityType.Builder
					.create(PotionCakeTileEntity::new, ModBlocks.POTION_CAKE_BLOCK.get()).build(null));

	public static final RegistryObject<TileEntityType<? extends OvenTileEntity>> OVEN = TILE_ENTITIES.register(
			"oven_block",
			() -> TileEntityType.Builder.create(OvenTileEntity::new, ModBlocks.OVEN_BLOCK.get()).build(null));
}