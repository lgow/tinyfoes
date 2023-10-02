package com.lgow.endofherobrine.entity.possessed;

import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.PossessedMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PosZombieVillager extends ZombieVillager  implements PossessedMob {
	public PosZombieVillager(EntityType<? extends PosZombieVillager> type, Level level) { super(type, level); }

	public static AttributeSupplier.Builder setCustomAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 35).add(Attributes.ARMOR, 4).add(
				Attributes.MOVEMENT_SPEED, 0.23).add(Attributes.ATTACK_DAMAGE, 6).add(Attributes.FOLLOW_RANGE, 40).add(
				Attributes.SPAWN_REINFORCEMENTS_CHANCE);
	}

	@Override
	public SoundEvent getAmbientSound() { return null; }

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return new ResourceLocation("entities/zombie_villager");
	}

	protected void registerGoals() {
		this.registerPosMonsterGoals(this, true);
		this.goalSelector.addGoal(0, new ZombieAttackGoal(this, 1, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	protected boolean isSunSensitive() { return true; }

	@Override
	public MobType getMobType() { return ModMobTypes.POSSESSED; }
}
