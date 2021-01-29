package com.loucaskreger.potioncakes.capability;

import java.util.List;

import net.minecraft.potion.EffectInstance;

public interface IPotionInformationHandler {

	void setEffects(EffectInstance... effects);
	
	void setEffects(List<EffectInstance> effects);

	List<EffectInstance> getEffects();

	int getColor();
	
	void setColor(int color);
	
	void setColor(List<EffectInstance> effects);

	void setEffectsAndColor(List<EffectInstance> effects);

}
