package com.lgow.endofherobrine.entity;

import com.lgow.endofherobrine.config.ModConfigs;
import com.lgow.endofherobrine.entity.ai.neutral.DefendPassiveMobsGoal;
import com.lgow.endofherobrine.entity.ai.neutral.PosAnimalPanicGoal;
import com.lgow.endofherobrine.entity.ai.neutral.PosAnimalTargetGoal;
import com.lgow.endofherobrine.entity.ai.neutral.PosAnimalWalkAroundGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

public interface PossessedAnimal extends PossessedMob, NeutralMob {
	@Override
	default boolean canRevertPossession() {
		return getPossessionTimer() >= ModConfigs.getRemainPossessedTicks() && !this.isAngry();
	}

	default void addPossessionSavedData(CompoundTag pCompound, int possessionTimer) {
		pCompound.putInt("PossessionTimer", possessionTimer);
	}

	default void readPossessionSaveData(CompoundTag pCompound) {
		this.setPossessionTimer(pCompound.getInt("PossessionTimer"));
	}

	void setPossessionTimer(int possessionTimer);

	int getPossessionTimer();

	default void registerPosAnimalGoals(PathfinderMob mob, double speed) {
		this.registerPosAnimalGoals(mob, speed, speed);
	}

	default void registerPosAnimalGoals(PathfinderMob mob, double attackSpeed, double panicSpeed) {
		this.registerPosMobGoals(mob, false, attackSpeed);
		mob.goalSelector.addGoal(3, new PosAnimalPanicGoal(mob, panicSpeed));
		mob.goalSelector.addGoal(5, new PosAnimalWalkAroundGoal(mob, 1.0D));
		mob.targetSelector.addGoal(0, new DefendPassiveMobsGoal(mob));
		mob.targetSelector.addGoal(1, new PosAnimalTargetGoal<>(mob, Player.class));
		mob.targetSelector.addGoal(2, new HurtByTargetGoal(mob, Monster.class).setAlertOthers());
	}
}
