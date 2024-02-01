package net.tinyallies.forge.capabilities;

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
	public final static Capability<IsBaby> IS_BABY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() { });
	public final static Capability<IsBabyfied> IS_BABYFIED_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() { });
	private IsBaby isBaby = null;
	private final LazyOptional<IsBaby> lazyChakraControl = LazyOptional.of(this::createChakraControl);
	private IsBabyfied narutoRunning = null;
	private final LazyOptional<IsBabyfied> lazyIsBabyfied = LazyOptional.of(this::createIsBabyfied);

	private IsBabyfied createIsBabyfied() {
		if (this.narutoRunning == null) {
			this.narutoRunning = new IsBabyfied();
		}
		return this.narutoRunning;
	}

	private IsBaby createChakraControl() {
		if (this.isBaby == null) {
			this.isBaby = new IsBaby();
		}
		return this.isBaby;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == IS_BABY_CAPABILITY) {
			return lazyChakraControl.cast();
		}
		if (cap == IS_BABYFIED_CAPABILITY) { return lazyIsBabyfied.cast(); }
		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		createChakraControl().saveNBTData(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		createChakraControl().loadNBTData(nbt);
	}
}
