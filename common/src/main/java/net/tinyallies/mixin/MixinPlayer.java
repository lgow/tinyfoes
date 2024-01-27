package net.tinyallies.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.tinyallies.capability.BabyfiedData;
import net.tinyallies.entity.BabyfiableEntity;
import net.tinyallies.registry.ModEffects;
import net.tinyallies.util.IEntityDataSaver;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements BabyfiableEntity {
	@Shadow @Final public static EntityDimensions STANDING_DIMENSIONS;
	@Shadow @Final private static Map<Pose, EntityDimensions> POSES;

	protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return POSES.getOrDefault(pose, STANDING_DIMENSIONS).scale(this.getScale());
	}

	@Override
	public float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
		switch (pose) {
			case SWIMMING, FALL_FLYING, SPIN_ATTACK -> {
				return 0.4f * this.getScale();
			}
			case CROUCHING -> {
				return 1.27f * this.getScale();
			}
		}
		return 1.62f * this.getScale();
	}

	@Inject(method = "serverAiStep", at = @At("HEAD"))
	void serverAiStep(CallbackInfo ci) {
		this.setBabyfied(this.hasEffect(ModEffects.BABYFICATION));
	}

	@Override
	public boolean isBaby() {
		return ((IEntityDataSaver) this).getPersistentData().getBoolean("IsBabyfied");
	}

	@Override
	public void setBabyfied(boolean bl) {
		BabyfiedData.updateIsBabyfied((IEntityDataSaver) this, bl);
		this.refreshDimensions();
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
		compoundTag.putBoolean("IsBabyfied", this.isBaby());
	}

	@Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
	public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
		this.setBabyfied(((IEntityDataSaver) this).getPersistentData().getBoolean("IsBabyfied"));
	}
}
