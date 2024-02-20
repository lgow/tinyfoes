package net.tinyallies.fabric.mixin;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.tinyallies.common.entity.BabyfiableEntity;
import net.tinyallies.common.registry.ModEffects;
import net.tinyallies.fabric.FabricTinyFoes;
import net.tinyallies.fabric.persistent_data.BabyfiedData;
import net.tinyallies.fabric.persistent_data.IEntityDataSaver;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements BabyfiableEntity, IEntityDataSaver {
	@Shadow @Final public static EntityDimensions STANDING_DIMENSIONS;
	@Shadow @Final private static Map<Pose, EntityDimensions> POSES;
	@Unique public boolean isBaby, isBabyfied;

	protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract int getPortalWaitTime();

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
	void aiStep(CallbackInfo ci) {
		this.$setBabyfied(this.hasEffect(ModEffects.BABYFICATION.get()));
		isBaby = getPersistentData().getBoolean("IsBaby");
		isBabyfied = FabricTinyFoes.getBabyfication(this);
	}

	@Override
	public boolean isBaby() {
		return $isBaby() || $isBabyfied();
	}

	@Unique
	public void $setBaby(boolean bl) {
		if (this.level != null && !this.level.isClientSide) {
			BabyfiedData.updateIsBaby((Player) (Object) this, bl);
			AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
			attributeInstance.removeModifier(SPEED_MODIFIER_BABY);
			if (bl) {
				attributeInstance.addTransientModifier(SPEED_MODIFIER_BABY);
			}
		}
		this.refreshDimensions();
	}

	@Override
	public boolean $isBaby() {
		return isBaby;
	}

	@Override
	public boolean $isBabyfied() {
		return isBabyfied;
	}

	@Override
	public void $setBabyfied(boolean bl) {
		FabricTinyFoes.setBabyfication(this, bl);
		if (this.level != null && !this.level.isClientSide) {
			AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
			attributeInstance.removeModifier(SPEED_MODIFIER_BABY);
			if (bl) {
				attributeInstance.addTransientModifier(SPEED_MODIFIER_BABY);
			}
		}
		this.refreshDimensions();
	}

	@Override
	public double getMyRidingOffset() {
		return isBaby() ? 0.0F : -0.35;
	}
}
