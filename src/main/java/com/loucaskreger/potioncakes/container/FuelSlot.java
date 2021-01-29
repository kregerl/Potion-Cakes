package com.loucaskreger.potioncakes.container;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FuelSlot extends SlotItemHandler {

	public FuelSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	public boolean isItemValid(ItemStack stack) {
		return AbstractFurnaceTileEntity.isFuel(stack) || isBucket(stack);
	}

	public int getItemStackLimit(ItemStack stack) {
		return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
	}

	public static boolean isBucket(ItemStack stack) {
		return stack.getItem() == Items.BUCKET;
	}

}
