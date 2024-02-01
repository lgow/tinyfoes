package net.tinyallies.fabric.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.tinyallies.fabric.persistent_data.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity implements IEntityDataSaver {
	private CompoundTag persistentData;

	@Override
	public CompoundTag getPersistentData() {
		if (this.persistentData == null) {
			this.persistentData = new CompoundTag();
		}
		return persistentData;
	}

	@Inject(method = "saveWithoutId", at = @At("HEAD"))
	protected void saveWithoutId(CompoundTag compoundTag, CallbackInfoReturnable<CompoundTag> cir) {
		if (persistentData != null) {
			compoundTag.put("tinyfoes.data", persistentData);
		}
	}

	@Inject(method = "load", at = @At("HEAD"))
	protected void load(CompoundTag nbt, CallbackInfo info) {
		if (nbt.contains("tinyfoes.data", 10)) {
			persistentData = nbt.getCompound("tinyfoes.data");
		}
	}
}
