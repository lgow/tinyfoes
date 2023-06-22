package net.tinyallies.entity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.EntityGetter;
import net.tinyallies.entity.ai.*;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface BabyMonster {

	boolean isTamed();

	void setTamed(boolean pTamed);

	LivingEntity getMonsterParent();

	void setMonsterParent(LivingEntity living);

	@Nullable
	UUID getParentUUID();

	void setParentUUID(@Nullable UUID pUuid);

	EntityGetter getLevel();

	boolean isInSittingPose();

	void setInSittingPose(boolean pSitting);

	boolean isOrderedToSit();

	boolean isFood(ItemStack pStack);

	default void defaultBabyGoals(PathfinderMob entity) {
////		entity.goalSelector.addGoal(1, new LookAtPlayerGoal(entity, Player.class ,100, 100));
//
//		entity.goalSelector.addGoal(1, new FloatGoal(entity));
//		entity.goalSelector.addGoal(3, new BabySitsWhenOrderedToGoal(entity));
//		entity.goalSelector.addGoal(4, new FollowPlayerGoal(entity, 1.0D, 10.0F, 2.0F, false));
//		entity.goalSelector.addGoal(6, new BabyMonsterPanicGoal(entity, 1.5D));
//		entity.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(entity, 0.8D));
//		entity.goalSelector.addGoal(8, new LookAtPlayerGoal(entity, Player.class, 8.0F));
//		entity.goalSelector.addGoal(9, new RandomLookAroundGoal(entity));
//		entity.targetSelector.addGoal(1, new DefendParentTargetGoal(entity));
//		entity.targetSelector.addGoal(2, new HurtByTargetGoal(entity));
//		entity.targetSelector.addGoal(3, new HelpAttackTargetGoal(entity));
//		entity.targetSelector.addGoal(4, new WildBabyTargeGoal<>(entity, Player.class, false));
//
////		entity.goalSelector.addGoal(1, new FloatGoal(entity));
////		entity.goalSelector.addGoal(1, new BabyMonsterPanicGoal(entity, 1.5D));
//////		entity.goalSelector.addGoal(2, new BabySitsWhenOrderedToGoal(entity));
////		entity.goalSelector.addGoal(6, new FollowPlayerGoal(entity, 1.0D, 10.0F, 2.0F, false));
////		entity.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(entity, 1.0D));
//		entity.goalSelector.addGoal(10, new LookAtPlayerGoal(entity, Player.class, 8.0F));
//		entity.goalSelector.addGoal(10, new RandomLookAroundGoal(entity));
////		entity.targetSelector.addGoal(1, new DefendParentTargetGoal(entity));
////		entity.targetSelector.addGoal(2, new HelpAttackTargetGoal(entity));
//		entity.targetSelector.addGoal(3, (new HurtByTargetGoal(entity)).setAlertOthers());
////		entity.targetSelector.addGoal(4, new WildBabyTargeGoal<>(entity, Player.class, false));

	}

	default void addTamedSaveData(CompoundTag pCompound, boolean orderedToSit) {
		if (this.getParentUUID() != null) {
			pCompound.putUUID("Parent", this.getParentUUID());
		}
		pCompound.putBoolean("Sitting", orderedToSit);
	}

	default void readTamedSaveData(CompoundTag pCompound, LivingEntity entity) {
		UUID uuid;
		if (pCompound.hasUUID("Parent")) {
			uuid = pCompound.getUUID("Parent");
		}
		else {
			String s = pCompound.getString("Parent");
			uuid = OldUsersConverter.convertMobOwnerIfNecessary(entity.getServer(), s);
		}
		if (uuid != null) {
			try {
				this.setParentUUID(uuid);
				this.setTamed(true);
			} catch (Throwable throwable) {
				this.setTamed(false);
			}
		}
	}

	@Nullable
	default LivingEntity getOwner() {
		UUID uuid = this.getParentUUID();
		return uuid == null ? null : this.getLevel().getPlayerByUUID(uuid);
	}

	Class<? extends PathfinderMob> getMonsterParentClass();


	default boolean canBeAdopted() {
		return this.getMonsterParent() == null;
	}


	default void spawnTamingParticles(boolean pTamed, LivingEntity entity) {
		ParticleOptions particleoptions = ParticleTypes.HEART;
		if (!pTamed) {
			particleoptions = ParticleTypes.SMOKE;
		}
		for (int i = 0; i < 7; ++i) {
			double d0 = entity.getRandom().nextGaussian() * 0.02D;
			double d1 = entity.getRandom().nextGaussian() * 0.02D;
			double d2 = entity.getRandom().nextGaussian() * 0.02D;
			entity.level.addParticle(particleoptions, entity.getRandomX(1.0D), entity.getRandomY() + 0.5D,
					entity.getRandomZ(1.0D), d0, d1, d2);
		}
	}

	default void spawnInvertedHealAndHarmParticles(LivingEntity entity) {
		for (int i = 0; i < 7; ++i) {
			double d0 = entity.getRandom().nextGaussian() * 0.02D;
			double d1 = entity.getRandom().nextGaussian() * 0.02D;
			double d2 = entity.getRandom().nextGaussian() * 0.02D;
			entity.level.addParticle(ParticleTypes.DAMAGE_INDICATOR, entity.getRandomX(1.0D),
					entity.getRandomY() + 0.5D, entity.getRandomZ(1.0D), d0, d1, d2);
		}
	}

	default boolean wantsToAttack(LivingEntity pTarget, LivingEntity pOwner) {
		if (pTarget instanceof OwnableEntity ownable) {
			return ownable.getOwner() != pOwner;
		}
		else if (pTarget instanceof BabyMonster ownable) {
			return ownable.getOwner() != pOwner;
		}
		return true;
	}

	void reassessTameGoals();

	void adopt(Player pPlayer);
}
