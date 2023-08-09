package com.lgow.endofherobrine.entity.possessed;

import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.PossessedMob;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PosPigman extends Zombie implements PossessedMob {
	public PosPigman(EntityType<? extends PosPigman> type, Level level) { super(type, level); }

	public static AttributeSupplier.Builder setCustomAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.MOVEMENT_SPEED, 0.35F)
				.add(Attributes.FOLLOW_RANGE, 40).add(Attributes.ATTACK_DAMAGE, 5.0D).add(
						Attributes.SPAWN_REINFORCEMENTS_CHANCE);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource pSource) {
		return pSource.is(DamageTypeTags.IS_FIRE) || super.isInvulnerableTo(pSource);
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return new ResourceLocation("minecraft", "entities/zombiefied_piglin");
	}

	@Override
	protected void registerGoals() {
		this.registerPosMobGoals(this, true);
		this.goalSelector.addGoal(0, new ZombieAttackGoal(this, 1.5, true));
		this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	protected SoundEvent getAmbientSound() { return null; }

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ZOMBIFIED_PIGLIN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ZOMBIFIED_PIGLIN_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.PIG_STEP, 0.15F, 1.0F);
	}

	@Override
	public MobType getMobType() { return ModMobTypes.POSSESSED; }

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dim) { return this.isBaby() ? 0.93F : 1.8F; }
}
