package com.lgow.endofherobrine.entity.possessed.animal;

import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.PossessedAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class PosSheep extends Sheep implements NeutralMob, PossessedAnimal {
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(40, 80);
	private static final EntityDataAccessor<Integer> CLONE_NUMBER = SynchedEntityData.defineId(PosSheep.class,
			EntityDataSerializers.INT);
	@Nullable private UUID persistentAngerTarget;
	private int remainingPersistentAngerTime;

	public PosSheep(EntityType<? extends PosSheep> type, Level level) { super(type, level); }

	public static AttributeSupplier.Builder setCustomAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8D).add(Attributes.MOVEMENT_SPEED, 0.23F)
				.add(Attributes.FOLLOW_RANGE, 40).add(Attributes.ATTACK_DAMAGE, 3D);
	}

	public int getCloneNumber() { return this.entityData.get(CLONE_NUMBER); }

	public void setCloneNumber(int num) { this.entityData.set(CLONE_NUMBER, num); }

	@Override
	protected void registerGoals() {
		this.registerPosAnimalGoals(this, 1.25D);
	}

	@Override
	protected void customServerAiStep() { }

	@Override
	public void aiStep() {
		super.aiStep();
		if (!this.level().isClientSide) {
			this.updatePersistentAnger((ServerLevel) this.level(), true);
			if (this.getCloneNumber() == 0) {
				Sheep sheep = (Sheep) this.convertBack(this, EntityType.SHEEP, !this.isAngry());
				sheep.setColor(this.getColor());
				sheep.setSheared(this.isSheared());
			}
			else if (this.tickCount > (2400 / this.getCloneNumber()) + random.nextInt(200)) {
				this.level().broadcastEntityEvent(this, (byte) 60);
				this.discard();
			}
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(CLONE_NUMBER, 0);
	}

	@Override
	public ResourceLocation getDefaultLootTable() { return new ResourceLocation("minecraft", "entities/sheep"); }

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("CloneNumber", this.getCloneNumber());
		this.addPersistentAngerSaveData(tag);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setCloneNumber(tag.getInt("CloneNumber"));
		this.readPersistentAngerSaveData(this.level(), tag);
	}

	@Override
	protected SoundEvent getAmbientSound() { return null; }

	@Override
	public Sheep getBreedOffspring(ServerLevel server, AgeableMob mob) {
		return EntityInit.SHEEP.get().create(server);
	}

	@Override
	public @NotNull List<ItemStack> onSheared(@Nullable Player player, @NotNull ItemStack item, Level level, BlockPos pos, int fortune) {
		this.setLastHurtByMob(player);
		this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
		return super.onSheared(player, item, level, pos, fortune);
	}

	@Override
	public boolean shouldDropExperience() { return super.shouldDropExperience() && this.getCloneNumber() == 0; }

	@Override
	protected boolean shouldDropLoot() { return super.shouldDropLoot() && this.getCloneNumber() == 0; }

	@Override
	public void dropAllDeathLoot(DamageSource source) {
		if (this.getCloneNumber() <= 3 && (source.getEntity() instanceof Player || this.lastHurtByPlayer != null)) {
			for (int i = 0; i < 2; i++) {
				createSheep();
			}
		}
		super.dropAllDeathLoot(source);
	}

	@Override
	public boolean readyForShearing() {
		return this.getCloneNumber() == 0 && super.readyForShearing();
	}

	@Override
	public MobType getMobType() { return ModMobTypes.POSSESSED; }

	private void createSheep() {
		PosSheep sheep = EntityInit.SHEEP.get().create(level());
		sheep.setColor(this.getColor());
		sheep.setSheared(this.isSheared());
		sheep.setBaby(this.isBaby());
		sheep.setCustomName(this.getCustomName());
		sheep.copyPosition(this);
		sheep.setCloneNumber(this.getCloneNumber() + 1);
		level().addFreshEntity(sheep);
	}

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
