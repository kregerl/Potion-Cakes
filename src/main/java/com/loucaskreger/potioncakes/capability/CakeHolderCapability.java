package com.loucaskreger.potioncakes.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CakeHolderCapability implements ICapabilitySerializable<CompoundNBT> {

	@CapabilityInject(ICakeHolder.class)
	public static final Capability<ICakeHolder> CAKE_HOLDER_CAPABILITY = null;

	private LazyOptional<ICakeHolder> holder = LazyOptional.of(CakeHolder::new);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return CAKE_HOLDER_CAPABILITY.orEmpty(cap, holder);
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) CAKE_HOLDER_CAPABILITY.getStorage().writeNBT(CAKE_HOLDER_CAPABILITY,
				holder.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		CAKE_HOLDER_CAPABILITY.getStorage().readNBT(CAKE_HOLDER_CAPABILITY,
				holder.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);

	}

}
