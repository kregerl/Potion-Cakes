package com.loucaskreger.potioncakes.init;

import com.loucaskreger.potioncakes.PotionCakes;
import com.loucaskreger.potioncakes.container.OvenContainer;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerTypes {

	public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister
			.create(ForgeRegistries.CONTAINERS, PotionCakes.MOD_ID);

	public static final RegistryObject<ContainerType<OvenContainer>> OVEN_CONTAINER = CONTAINER_TYPES
			.register("oven_container", () -> IForgeContainerType.create(OvenContainer::new));

}
