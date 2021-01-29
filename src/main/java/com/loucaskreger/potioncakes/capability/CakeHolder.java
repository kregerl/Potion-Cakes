package com.loucaskreger.potioncakes.capability;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

public class CakeHolder implements ICakeHolder {

	private static final ImmutableList<Double> SLICE_VALUES = ImmutableList.of(0.0625, 0.1875, 0.3125, 0.4375, 0.5625,
			0.6875, 0.8125);
	private int slices = -1; 
	private List<EffectInstance> effects;

	public CakeHolder() {
		initalizeState();
	}

	@Override
	public int getSlices() {
		return slices;
	}

	@Override
	public void setSlices(int slices) {
		this.slices = slices;
	}

	@Override
	public void setSlices(double slices) {
		this.slices = SLICE_VALUES.indexOf(slices);
	}

	@Override
	public List<EffectInstance> getEffects() {
		return this.effects;
	}

	@Override
	public void setEffects(List<EffectInstance> effects) {
		this.effects = effects;

	}

	@Override
	public void clearEffects() {
		initalizeState();

	}

	private void initalizeState() {
		this.effects = new ArrayList<EffectInstance>();
	}

	public static class Storage implements Capability.IStorage<ICakeHolder> {

		private static final String SLICE_KEY = "slices";
		private static final String EFFECT_KEY = "effects";

		@Override
		public INBT writeNBT(Capability<ICakeHolder> capability, ICakeHolder instance, Direction side) {
			ListNBT tagList = new ListNBT();
			List<EffectInstance> effects = instance.getEffects();
			for (EffectInstance e : effects) {
				CompoundNBT tag = new CompoundNBT();
				e.write(tag);
				tagList.add(tag);
			}
			CompoundNBT nbt = new CompoundNBT();
			nbt.putInt(SLICE_KEY, instance.getSlices());
			nbt.put(EFFECT_KEY, tagList);
			return nbt;
		}

		@Override
		public void readNBT(Capability<ICakeHolder> capability, ICakeHolder instance, Direction side, INBT nbt) {
			int slices = ((CompoundNBT) nbt).getInt(SLICE_KEY);
			ListNBT tagList = ((CompoundNBT) nbt).getList(EFFECT_KEY, Constants.NBT.TAG_COMPOUND);
			List<EffectInstance> effects = new ArrayList<EffectInstance>();
			for (int i = 0; i < tagList.size(); i++) {
				CompoundNBT tag = tagList.getCompound(i);
				effects.add(EffectInstance.read(tag));
			}
			instance.setSlices(slices);
			instance.setEffects(effects);

		}

	}

}
