package com.loucaskreger.potioncakes.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class SpelunkerEffect extends Effect {

	public SpelunkerEffect() {
		super(EffectType.BENEFICIAL, 0xd6ff21);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration > 0;
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {
		super.performEffect(entity, amplifier);
	}

	public static class Color {

		private float red;
		private float green;
		private float blue;
		private float alpha;

		public Color(float red, float green, float blue, float alpha) {
			this.setRed(red);
			this.setGreen(green);
			this.setBlue(blue);
			this.setAlpha(alpha);
		}

		public float getRed() {
			return red;
		}

		public void setRed(float red) {
			this.red = red;
		}

		public float getGreen() {
			return green;
		}

		public void setGreen(float green) {
			this.green = green;
		}

		public float getBlue() {
			return blue;
		}

		public void setBlue(float blue) {
			this.blue = blue;
		}

		public float getAlpha() {
			return alpha;
		}

		public void setAlpha(float alpha) {
			this.alpha = alpha;
		}

	}

}
