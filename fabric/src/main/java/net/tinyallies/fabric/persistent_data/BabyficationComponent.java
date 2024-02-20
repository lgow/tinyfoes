package net.tinyallies.fabric.persistent_data;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.tinyallies.fabric.FabricTinyFoes;

public class BabyficationComponent implements PlayerComponent, AutoSyncedComponent, BooleanComponent {
	private final Entity provider;
	private boolean value;

	@Override
	public boolean getValue() { return this.value; }

	public BabyficationComponent(Entity provider) { this.provider = provider; }

	@Override
	public void setValue(boolean b) {
		value = b;
		FabricTinyFoes.BABYFICATION.sync(this.provider);
	}

	@Override
	public void readFromNbt(CompoundTag tag) { this.value = tag.getBoolean("value"); }

	@Override
	public void writeToNbt(CompoundTag tag) { tag.putBoolean("value", this.value); }
}