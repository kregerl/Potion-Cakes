package com.loucaskreger.potioncakes.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PotionInfomationCapability implements ICapabilitySerializable<CompoundNBT> {

	@CapabilityInject(IPotionInformationHandler.class)
	public static final Capability<IPotionInformationHandler> POTION_CAPABILITY = null;

	private LazyOptional<IPotionInformationHandler> holder = LazyOptional.of(PotionInformationHandler::new);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return POTION_CAPABILITY.orEmpty(cap, holder);
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) POTION_CAPABILITY.getStorage().writeNBT(POTION_CAPABILITY,
				holder.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		POTION_CAPABILITY.getStorage().readNBT(POTION_CAPABILITY,
				holder.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);

	}

}
