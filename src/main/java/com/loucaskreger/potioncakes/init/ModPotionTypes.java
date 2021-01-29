package com.loucaskreger.potioncakes.init;

import com.loucaskreger.potioncakes.PotionCakes;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModPotionTypes {

	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES,
			PotionCakes.MOD_ID);

	public static final RegistryObject<Potion> SPELUNKER_POTION = POTIONS.register("spelunker_potion",
			() -> new Potion(new EffectInstance(ModEffects.SPELUNKER_EFFECT.get(), 900)));

	public static final RegistryObject<Potion> LONG_SPELUNKER_POTION = POTIONS.register("long_spelunker_potion",
			() -> new Potion(new EffectInstance(ModEffects.SPELUNKER_EFFECT.get(), 1800)));

	public static final RegistryObject<Potion> STRONG_SPELUNKER_POTION = POTIONS.register("strong_spelunker_potion",
			() -> new Potion(new EffectInstance(ModEffects.SPELUNKER_EFFECT.get(), 450, 1)));

}
