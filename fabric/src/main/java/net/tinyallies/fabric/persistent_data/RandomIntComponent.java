package net.tinyallies.fabric.persistent_data;

import net.minecraft.nbt.CompoundTag;

public class RandomIntComponent implements BooleanComponent {
	private boolean value;

	@Override
	public boolean getValue() { return this.value; }

	@Override
	public void setValue(boolean b) {
		value = b;
	}

	@Override
	public void readFromNbt(CompoundTag tag) { this.value = tag.getBoolean("value"); }

	@Override
	public void writeToNbt(CompoundTag tag) { tag.putBoolean("value", this.value); }
}