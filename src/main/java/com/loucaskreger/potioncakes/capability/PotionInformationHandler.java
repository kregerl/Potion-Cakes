package com.loucaskreger.potioncakes.capability;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

public class PotionInformationHandler implements IPotionInformationHandler {

	/**
	 * Convert this all over to a list of EffectInstances instead of an array. make
	 * add() and remove()
	 */
	// Add all methods to interface, save color int in nbt storage subclass aswell
	private List<EffectInstance> effects;
	private int color;

	public PotionInformationHandler() {
		this.effects = new ArrayList<EffectInstance>();
	}

	public PotionInformationHandler(CompoundNBT nbt) {
		this.effects = PotionUtils.getEffectsFromTag(nbt);
	}

	@Override
	public void setEffects(EffectInstance... effects) {
		for (EffectInstance e : effects) {
			this.effects.add(e);
		}

	}

	@Override
	public void setEffects(List<EffectInstance> effects) {
		this.effects = effects;

	}

	@Override
	public void setEffectsAndColor(List<EffectInstance> effects) {
		this.setEffects(effects);
		this.setColor(effects);
	}

	@Override
	public int getColor() {
		return this.color;
	}

	@Override
	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public void setColor(List<EffectInstance> effects) {
		if (!effects.isEmpty())
			this.color = effects.get(0).getPotion().getLiquidColor();
	}

	@Override
	public List<EffectInstance> getEffects() {
		return this.effects;
	}

	public static class Storage implements Capability.IStorage<IPotionInformationHandler> {

		private static final String EFFECT_KEY = "effects";
		private static final String COLOR_KEY = "color";

		@Override
		public INBT writeNBT(Capability<IPotionInformationHandler> capability, IPotionInformationHandler instance,
				Direction side) {
			ListNBT tagList = new ListNBT();
			List<EffectInstance> effects = instance.getEffects();
			for (EffectInstance e : effects) {
				CompoundNBT tag = new CompoundNBT();
				e.write(tag);
				tagList.add(tag);
			}
			CompoundNBT nbt = new CompoundNBT();
			nbt.putInt(COLOR_KEY, instance.getColor());
			nbt.put(EFFECT_KEY, tagList);
			return nbt;
		}

		@Override
		public void readNBT(Capability<IPotionInformationHandler> capability, IPotionInformationHandler instance,
				Direction side, INBT nbt) {
			ListNBT tagList = ((CompoundNBT) nbt).getList(EFFECT_KEY, Constants.NBT.TAG_COMPOUND);
			int color = ((CompoundNBT) nbt).getInt(COLOR_KEY);
			int listLength = tagList.size();
			List<EffectInstance> effects = new ArrayList<EffectInstance>();
			for (int i = 0; i < listLength; i++) {
				CompoundNBT tag = tagList.getCompound(i);
				effects.add(EffectInstance.read(tag));
			}
			instance.setColor(color);
			instance.setEffects(effects);

		}

	}

}
