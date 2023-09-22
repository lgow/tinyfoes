package com.lgow.endofherobrine.entity.possessed.animal;

import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.PossessedAnimal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PosCow extends Cow implements NeutralMob, PossessedAnimal {
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(40, 80);

	private @Nullable UUID persistentAngerTarget;

	private int remainingPersistentAngerTime;

	public PosCow(EntityType<? extends PosCow> type, Level level) { super(type, level); }

	public static AttributeSupplier.Builder setCustomAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30).add(Attributes.MOVEMENT_SPEED, 0.2).add(
				Attributes.FOLLOW_RANGE, 40).add(Attributes.ATTACK_DAMAGE, 10);
	}

	@Override
	protected void registerGoals() {
		this.registerPosAnimalGoals(this, 2.0D);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!this.level().isClientSide) {
			this.updatePersistentAnger((ServerLevel) this.level(), true);
		}
	}

	@Override
	protected SoundEvent getAmbientSound() { return null; }

	@Override
	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		this.setLastHurtByMob(pPlayer);
		return super.mobInteract(pPlayer, pHand);
	}

	@Override
	public Cow getBreedOffspring(ServerLevel server, AgeableMob mob) {
		return EntityInit.P_COW.get().create(server);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		this.addPersistentAngerSaveData(pCompound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.readPersistentAngerSaveData(this.level(), pCompound);
	}

	@Override
	public void dropAllDeathLoot(DamageSource pSource) {
		super.dropAllDeathLoot(pSource);
		if (!pSource.is(DamageTypes.OUTSIDE_BORDER)) {
			level().setBlockAndUpdate(this.blockPosition(), Blocks.LAVA.defaultBlockState());
		}
	}

	@Override
	public MobType getMobType() { return ModMobTypes.POSSESSED; }

	@Override
	protected ResourceLocation getDefaultLootTable() { return new ResourceLocation("minecraft", "entities/cow"); }

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
}
