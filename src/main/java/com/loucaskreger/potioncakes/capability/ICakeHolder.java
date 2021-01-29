package com.loucaskreger.potioncakes.capability;

import java.util.List;

import net.minecraft.potion.EffectInstance;

public interface ICakeHolder {
	
	int getSlices();
	
	void setSlices(int slices);
	
	void setSlices(double slices);
	
	List<EffectInstance> getEffects();
	
	void setEffects(List<EffectInstance> effects);
	
	void clearEffects();
	

}
