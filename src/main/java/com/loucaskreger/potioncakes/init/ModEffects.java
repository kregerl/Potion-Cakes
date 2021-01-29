package com.loucaskreger.potioncakes.init;

import com.loucaskreger.potioncakes.PotionCakes;
import com.loucaskreger.potioncakes.effect.SpelunkerEffect;

import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEffects {
	
	public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS,
			PotionCakes.MOD_ID);

	public static final RegistryObject<Effect> SPELUNKER_EFFECT = EFFECTS.register("spelunker",
			SpelunkerEffect::new);

}
