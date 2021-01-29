package com.loucaskreger.potioncakes.item;

import com.loucaskreger.potioncakes.PotionCakes;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class ModBlockItem extends BlockItem {

	public ModBlockItem() {
		super(Blocks.AIR, new Item.Properties().group(PotionCakes.CAKES_TAB));
	}

	public ModBlockItem(Item.Properties properties) {
		super(Blocks.AIR, properties);
	}

	public ModBlockItem(Block blockIn) {
		super(blockIn, new Item.Properties().group(PotionCakes.CAKES_TAB));
	}

	public ModBlockItem(Block blockIn, Item.Properties properties) {
		super(blockIn, properties);
	}

}
