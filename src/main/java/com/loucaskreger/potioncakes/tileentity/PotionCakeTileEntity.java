package com.loucaskreger.potioncakes.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.loucaskreger.potioncakes.capability.IPotionInformationHandler;
import com.loucaskreger.potioncakes.capability.PotionInfomationCapability;
import com.loucaskreger.potioncakes.capability.PotionInformationHandler;
import com.loucaskreger.potioncakes.init.ModTileEntityTypes;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

public class PotionCakeTileEntity extends TileEntity {

	private static final String POTION_TAG = "potion";

	private PotionInformationHandler potionHandler = createPotion();
	private LazyOptional<IPotionInformationHandler> potion = LazyOptional.of(() -> potionHandler);

	public PotionCakeTileEntity() {
		super(ModTileEntityTypes.POTION_CAKE.get());
	}

	@Override
	public void remove() {
		super.remove();
		potion.invalidate();
	}

	private PotionInformationHandler createPotion() {
		return new PotionInformationHandler();
	}

	public void setEffects(List<EffectInstance> effects) {
		potionHandler.setEffects(effects);
	}

	public void setEffectsAndColor(List<EffectInstance> effects) {
		potionHandler.setEffectsAndColor(effects);
	}

	public List<EffectInstance> getEffects() {
		return potionHandler.getEffects();
	}

	public int getColor() {
		return potionHandler.getColor();
	}

	private ListNBT getNBTListFromEffects() {
		ListNBT nbtList = new ListNBT();
		for (EffectInstance e : this.getEffects()) {
			CompoundNBT tag = new CompoundNBT();
			e.write(tag);
			nbtList.add(tag);
		}
		return nbtList;
	}

	private List<EffectInstance> getEffectsFromNBTList(ListNBT tagList) {
		List<EffectInstance> effects = new ArrayList<EffectInstance>();
		for (int i = 0; i < tagList.size(); i++) {
			CompoundNBT tag = tagList.getCompound(i);
			effects.add(EffectInstance.read(tag));
		}
		return effects;
	}

	@Override
	public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
		ListNBT nbt = pkt.getNbtCompound().getList(POTION_TAG, Constants.NBT.TAG_COMPOUND);
		this.setEffectsAndColor(getEffectsFromNBTList(nbt));
	}

	@Override
	public void read(final CompoundNBT compound) {
		super.read(compound);
		ListNBT nbt = compound.getList(POTION_TAG, Constants.NBT.TAG_COMPOUND);
		this.setEffectsAndColor(getEffectsFromNBTList(nbt));

	}

	@Nonnull
	@Override
	public CompoundNBT write(final CompoundNBT compound) {
		super.write(compound);
		compound.put(POTION_TAG, getNBTListFromEffects());
		return compound;
	}

	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
		final CompoundNBT tag = new CompoundNBT();
		tag.put(POTION_TAG, getNBTListFromEffects());
		return new SUpdateTileEntityPacket(this.pos, 0, tag);
	}

	@Nonnull
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == PotionInfomationCapability.POTION_CAPABILITY) {
			return potion.cast();
		}
		return super.getCapability(cap, side);
	}

}
