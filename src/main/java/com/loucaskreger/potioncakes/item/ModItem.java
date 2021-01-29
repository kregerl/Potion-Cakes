package com.loucaskreger.potioncakes.item;

import com.loucaskreger.potioncakes.PotionCakes;

import net.minecraft.item.Item;

public class ModItem extends Item {

	public ModItem() {
		super(new Item.Properties().group(PotionCakes.CAKES_TAB));
	}
	public ModItem(Properties properties) {
		super(properties.group(PotionCakes.CAKES_TAB));

	}

}
