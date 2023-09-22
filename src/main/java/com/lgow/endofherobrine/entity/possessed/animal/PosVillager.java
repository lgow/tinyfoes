package com.lgow.endofherobrine.entity.possessed.animal;

import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.PossessedAnimal;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PosVillager extends Villager implements NeutralMob, PossessedAnimal {
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(40, 80);

	@Nullable private UUID persistentAngerTarget;

	private int remainingPersistentAngerTime;

	public PosVillager(EntityType<? extends PosVillager> type, Level level) {
		super(type, level);
		this.getNavigation().setCanFloat(true);
		((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
	}

	public static AttributeSupplier.Builder setCustomAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 35).add(Attributes.FOLLOW_RANGE, 40).add(
				Attributes.MOVEMENT_SPEED, 0.5D).add(Attributes.ATTACK_DAMAGE, 6.0D).add(Attributes.ARMOR, 4.0D).add(
				Attributes.SPAWN_REINFORCEMENTS_CHANCE);
	}

	@Override
	protected void registerGoals() {
		this.registerPosAnimalGoals(this, 1.5D);
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return new ResourceLocation("minecraft", "entities/villager");
	}

	@Override
	public void dropAllDeathLoot(DamageSource source) {
		if (source.getEntity() instanceof Player player) {
			MobEffectInstance badOmen = player.getEffect(MobEffects.BAD_OMEN);
			int effectLvl = 1;
			if (badOmen != null) {
				effectLvl += badOmen.getAmplifier();
				player.removeEffectNoUpdate(MobEffects.BAD_OMEN);
			}
			else {
				--effectLvl;
			}
			effectLvl = Mth.clamp(effectLvl, 0, 4);
			MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.BAD_OMEN, 30000, effectLvl, false, false,
					true);
			if (!this.level().getGameRules().getBoolean(GameRules.RULE_DISABLE_RAIDS)) {
				player.addEffect(mobeffectinstance);
			}
		}
		super.dropAllDeathLoot(source);
	}

	@Override
	protected Component getTypeName() { return this.getType().getDescription(); }

	@Override
	public MobType getMobType() { return ModMobTypes.POSSESSED; }

	@Override
	public void registerBrainGoals(Brain<Villager> pVillagerBrain) { }

	@Override
	public SoundEvent getAmbientSound() { return null; }

	@Override
	public int getRemainingPersistentAngerTime() { return this.remainingPersistentAngerTime; }

	@Override
	public void setRemainingPersistentAngerTime(int pTime) { this.remainingPersistentAngerTime = pTime; }

	@Nullable
	@Override
	public UUID getPersistentAngerTarget() { return this.persistentAngerTarget; }

	@Override
	public void setPersistentAngerTarget(@Nullable UUID pTarget) { this.persistentAngerTarget = pTarget; }

	@Override
	public void startPersistentAngerTimer() {
		this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
	}

	public Villager getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
		Villager villager = super.getBreedOffspring(pLevel,pOtherParent);
		PosVillager posVillager = villager.convertTo(EntityInit.P_VILlAGER.get(), true);
		posVillager.setVillagerData(villager.getVillagerData());
		return posVillager;
	}

	@Override
	public void startTrading(Player pPlayer) {
		if (!this.isAggressive()) {
			super.startTrading(pPlayer);
		}
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!this.level().isClientSide) {
			this.updatePersistentAnger((ServerLevel) this.level(), true);
		}
	}
}
