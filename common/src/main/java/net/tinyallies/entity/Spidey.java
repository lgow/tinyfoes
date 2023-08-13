package net.tinyallies.entity;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;
import net.tinyallies.entity.ai.LookForParentGoal;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Spidey extends Spider implements NeutralMob, BabyMonster {
	protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Spidey.class,
			EntityDataSerializers.BYTE);
	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(
			Spidey.class, EntityDataSerializers.OPTIONAL_UUID);
	private static EntityDimensions STANDING = EntityDimensions.scalable(0.9F, 0.45F);
	private static final Map<Pose, EntityDimensions> POSES = ImmutableMap.<Pose, EntityDimensions> builder().put(
			Pose.STANDING, STANDING).put(Pose.CROUCHING, EntityDimensions.scalable(0.9F, 0.35F)).build();
	private final AvoidEntityGoal<Player> avoidPlayersGoal = new AvoidEntityGoal<>(this, Player.class, 16.0F, 0.8D,
			1.33D);
	private final LookForParentGoal followParentGoal = new LookForParentGoal(this, 1.0F, this.getParentClass());
	private final NearestAttackableTargetGoal<Player> targetPlayerGoal = new NearestAttackableTargetGoal<>(this,
			Player.class, true);
	private boolean orderedToSit;
	private LivingEntity parent;

	public Spidey(EntityType<? extends Spider> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.reassessTameGoals();
		applyAttributeModifiers();
	}



	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new BabySpiderLeapAtTargetGoal(this, 0.4F));
		this.goalSelector.addGoal(0, new BabySpiderAttackGoal(this));
		this.defaultBabyGoals(this);
	}

	public void reassessTameGoals() {
			this.goalSelector.removeGoal(this.followParentGoal);
		this.goalSelector.removeGoal(this.avoidPlayersGoal);
		this.goalSelector.removeGoal(this.targetPlayerGoal);
		if (!this.isTamed()) {
			if (this.getParent() == null) {
				this.goalSelector.addGoal(4, this.avoidPlayersGoal);
			}else {
				this.goalSelector.addGoal(0, this.followParentGoal);
				this.targetSelector.addGoal(3, this.targetPlayerGoal);
			}
		}
	}

	public EntityDimensions getDimensions(Pose pPose) {
		return POSES.getOrDefault(pPose, STANDING);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource pSource) {
		return this.hasSameOwner(pSource.getEntity()) || super.isInvulnerableTo(pSource);
	}

	@Nullable
	public ItemStack getPickResult() {
		return new ItemStack(Items.SPIDER_SPAWN_EGG);
	}

	@Override
	protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
		return pPose == Pose.CROUCHING ? 0.29F : 0.34F;
	}

	public boolean isFood(ItemStack pStack) {
		Item item = pStack.getItem();
		return item.isEdible() && pStack.getItem().getFoodProperties().isMeat();
	}

	@Override
	public LivingEntity getParent() {
		return this.parent;
	}

	@Override
	public void setParent(LivingEntity living) {
		this.parent = living;
	}

	@Override
	public Class<? extends PathfinderMob> getParentClass() {
		return Spider.class;
	}

	public boolean hurt(DamageSource pSource, float pAmount) {
		return babyHurt(this, pSource, super.hurt(pSource, pAmount));
	}

	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		return babyInteract(pPlayer, pHand, super.mobInteract(pPlayer, pHand));
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_FLAGS_ID, (byte) 0);
		this.entityData.define(DATA_OWNERUUID_ID, Optional.empty());
	}

	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		addBabySaveData(pCompound, this.orderedToSit);
		addPersistentAngerSaveData(pCompound);
	}

	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		readBabySaveData(pCompound, this);
		orderedToSit = pCompound.getBoolean("Sitting");
		setInSittingPose(orderedToSit);
		readPersistentAngerSaveData(this.level, pCompound);
	}

	@Override
	public boolean canBeLeashed(Player pPlayer) {
		return !this.isLeashed() && pPlayer == this.getOwner();
	}

	@Override
	public void handleEntityEvent(byte pId) {
		super.handleEntityEvent(pId);
		handleBabyEvent(pId);
	}

	public void setDataFlagsId(boolean pTamed, byte mask) {
		byte flagsID = this.entityData.get(DATA_FLAGS_ID);
		this.entityData.set(DATA_FLAGS_ID, pTamed ? (byte) (flagsID | mask) : (byte) (flagsID & ~mask));
	}

	@Override
	public void tick() {
		if (this.getParent() != null && !this.getParent().isAlive()) {
			this.setParent(null);
			this.reassessTameGoals();
		}
		this.updatePose(this);
		super.tick();
	}

	@Override
	protected void tickLeash() {
		if (this.getLeashHolder() != null && this.isInSittingPose()) {
			if (this.distanceTo(getLeashHolder()) > 10.0f) {
				this.dropLeash(true, true);
			}
			return;
		}
		super.tickLeash();
	}

	@Override
	public int getRemainingPersistentAngerTime() {
		return 0;
	}

	@Override
	public void setRemainingPersistentAngerTime(int pRemainingPersistentAngerTime) {
	}

	@org.jetbrains.annotations.Nullable
	@Override
	public UUID getPersistentAngerTarget() {
		return null;
	}

	@Override
	public void setPersistentAngerTarget(@org.jetbrains.annotations.Nullable UUID pPersistentAngerTarget) {
	}

	@Override
	public void startPersistentAngerTimer() {
	}

	public boolean canAttack(LivingEntity livingEntity) {
		return !this.hasSameOwner(livingEntity)&& super.canAttack(livingEntity);
	}

	public boolean isOwnedBy(LivingEntity pEntity) {
		return pEntity == this.getOwner();
	}

	public Team getTeam() {
		return getBabyTeam(super.getTeam());
	}

	public boolean isAlliedTo(Entity pEntity) {
		return babyIsAlliedTo(pEntity, super.isAlliedTo(pEntity));
	}

	public void die(DamageSource pCause) {
		super.die(pCause);
		if (this.dead) { this.sendDeathMessage(this); }
	}

	@Override
	public boolean isPreventingPlayerRest(Player pPlayer) {
		return !this.isTamed();
	}

	public boolean isOrderedToSit() {
		return this.orderedToSit;
	}

	public void setOrderedToSit(boolean pOrderedToSit) {
		this.orderedToSit = pOrderedToSit;
	}

	//===================================================
	public boolean isTamed() {
		return (this.entityData.get(DATA_FLAGS_ID) & 4) != 0;
	}

	public void setTamed(boolean pTamed) {
		byte flagsID = this.entityData.get(DATA_FLAGS_ID);
		this.entityData.set(DATA_FLAGS_ID, pTamed ? (byte) (flagsID | 4) : (byte) (flagsID & -1));
	}

	@Nullable
	public UUID getOwnerUUID() {
		return this.entityData.get(DATA_OWNERUUID_ID).orElse(null);
	}

	public void setOwnerUUID(@Nullable UUID pUuid) {
		this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(pUuid));
	}

	public boolean isInSittingPose() {
		return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
	}

	public void setInSittingPose(boolean pSitting) {
		byte flagsID = this.entityData.get(DATA_FLAGS_ID);
		this.entityData.set(DATA_FLAGS_ID, pSitting ? (byte) (flagsID | 1) : (byte) (flagsID & -2));
	}

	static class BabySpiderAttackGoal extends MeleeAttackGoal {
		private final Spidey spidey;

		public BabySpiderAttackGoal(Spidey pSpider) {
			super(pSpider, 1.0D, true);
			this.spidey = pSpider;
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean canUse() {
			return super.canUse() && !this.mob.isVehicle();
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean canContinueToUse() {
			float f = this.mob.getLightLevelDependentMagicValue();
			if (f >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
				this.mob.setTarget(null);
				return false;
			}
			else {
				return !this.spidey.isOrderedToSit() && super.canContinueToUse();
			}
		}

		protected double getAttackReachSqr(LivingEntity pAttackTarget) {
			return 4.0F + pAttackTarget.getBbWidth();
		}
	}

	static class BabySpiderLeapAtTargetGoal extends LeapAtTargetGoal {
		private final Spidey spidey;

		public BabySpiderLeapAtTargetGoal(Spidey pSpidey, float pYd) {
			super(pSpidey, pYd);
			this.spidey = pSpidey;
		}

		@Override
		public boolean canContinueToUse() {
			return !this.spidey.isOrderedToSit() && super.canContinueToUse();
		}
	}
}
