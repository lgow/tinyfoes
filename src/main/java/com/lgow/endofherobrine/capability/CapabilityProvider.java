package com.lgow.endofherobrine.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
	public final static Capability<Wrath> WRATH = CapabilityManager.get(new CapabilityToken<>() { });
	private Wrath wrath = null;
	private final LazyOptional<Wrath> lazyWrath = LazyOptional.of(this::createWrath);

	private Wrath createWrath() {
		if (this.wrath == null) {
			this.wrath = new Wrath();
		}
		return this.wrath;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == WRATH) {
			return lazyWrath.cast();
		}
		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		createWrath().saveNBTData(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		createWrath().loadNBTData(nbt);
	}
}