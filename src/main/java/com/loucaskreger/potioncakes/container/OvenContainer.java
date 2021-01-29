package com.loucaskreger.potioncakes.container;

import java.util.Objects;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import javax.annotation.Nonnull;

import com.loucaskreger.potioncakes.init.ModBlocks;
import com.loucaskreger.potioncakes.init.ModContainerTypes;
import com.loucaskreger.potioncakes.tileentity.OvenTileEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.items.SlotItemHandler;

public class OvenContainer extends Container {

	private OvenTileEntity ovenTileEntity;
	private IWorldPosCallable canInteractWithCallable;
	private FunctionalIntReferenceHolder currentCookTime;

	public OvenContainer(int id, PlayerInventory playerInventory, OvenTileEntity tileEntity) {
		super(ModContainerTypes.OVEN_CONTAINER.get(), id);

		this.ovenTileEntity = tileEntity;
		this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());

		final int slotSizePlusOffset = 18;
		final int startX = 8;
		final int mainInventoryStartY = 84;
		int hotbarY = 142;
		// Hotbar
		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, startX + (i * slotSizePlusOffset), hotbarY));
		}
		// Player Inv
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		int ovenStartX = 30;
		int ovenStartY = 17;

		for (int l = 0; l < 3; l++) {
			for (int m = 0; m < 3; m++) {
				this.addSlot(new SlotItemHandler(tileEntity.getInventory(), m + (l * 3),
						ovenStartX + (m * slotSizePlusOffset), ovenStartY + (l * slotSizePlusOffset)));

			}
		}
		// fuel slot
		this.addSlot(new FuelSlot(tileEntity.getInventory(), 9, 92, 53));
		// result slot
		this.addSlot(new SlotItemHandler(tileEntity.getInventory(), 10, 124, 21));

		this.trackInt(currentCookTime = new FunctionalIntReferenceHolder(() -> this.ovenTileEntity.smeltTime,
				num -> this.ovenTileEntity.smeltTime = num));

	}

	public OvenContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
		this(id, playerInventory, getTileEntity(playerInventory, buffer));
	}

	private static OvenTileEntity getTileEntity(PlayerInventory playerInventory, PacketBuffer buffer) {
		Objects.requireNonNull(playerInventory);
		Objects.requireNonNull(buffer);
		TileEntity tileEntity = playerInventory.player.world.getTileEntity(buffer.readBlockPos());
		if (tileEntity instanceof OvenTileEntity) {
			return (OvenTileEntity) tileEntity;
		}
		throw new IllegalStateException("Tile entity type is not correct at : " + tileEntity.toString());
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(canInteractWithCallable, playerIn, ModBlocks.OVEN_BLOCK.get());
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index == 0) {
				canInteractWithCallable.consume((p_217067_2_, p_217067_3_) -> {
					itemstack1.getItem().onCreated(itemstack1, p_217067_2_, playerIn);
				});
				if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 10 && index < 46) {
				if (!this.mergeItemStack(itemstack1, 1, 10, false)) {
					if (index < 37) {
						if (!this.mergeItemStack(itemstack1, 37, 46, false)) {
							return ItemStack.EMPTY;
						}
					} else if (!this.mergeItemStack(itemstack1, 10, 37, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);
			if (index == 0) {
				playerIn.dropItem(itemstack2, false);
			}
		}

		return itemstack;
	}

	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return slotIn.inventory != this.ovenTileEntity && super.canMergeSlot(stack, slotIn);
	}

	public class FunctionalIntReferenceHolder extends IntReferenceHolder {

		private final IntSupplier getter;
		private final IntConsumer setter;

		public FunctionalIntReferenceHolder(final IntSupplier getter, final IntConsumer setter) {
			this.getter = getter;
			this.setter = setter;
		}

		@Override
		public int get() {
			return this.getter.getAsInt();
		}

		@Override
		public void set(int value) {
			this.setter.accept(value);

		}

	}
}
