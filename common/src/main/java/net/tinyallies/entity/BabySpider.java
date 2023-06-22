package net.tinyallies.entity;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.scores.Team;
import net.tinyallies.entity.ai.LookForParentGoal;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BabySpider extends Spider implements NeutralMob, BabyMonster {
	protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(BabySpider.class,
			EntityDataSerializers.BYTE);

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(
			BabySpider.class, EntityDataSerializers.OPTIONAL_UUID);

	protected static EntityDimensions STANDING = EntityDimensions.scalable(0.9F, 0.45F);

	private static final Map<Pose, EntityDimensions> POSES = ImmutableMap.<Pose, EntityDimensions> builder().put(
			Pose.STANDING, STANDING).put(Pose.SITTING, EntityDimensions.scalable(0.9F, 0.35F)).build();

	private boolean orderedToSit;

	private LivingEntity animalParent;

	private AvoidEntityGoal<Player> avoidPlayersGoal;

	private LookForParentGoal followParentGoal;

	public BabySpider(EntityType<? extends Spider> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.reassessTameGoals();
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(3, new BabySpiderLeapAtTargetGoal(this, 0.4F));
		this.goalSelector.addGoal(4, new BabySpiderAttackGoal(this));
		this.defaultBabyGoals(this);
	}

	public void reassessTameGoals() {
		if (this.avoidPlayersGoal == null) {
			this.avoidPlayersGoal = new AvoidEntityGoal<>(this, Player.class, 16.0F, 0.8D, 1.33D);
		}
		if (this.followParentGoal == null) {
			this.followParentGoal = new LookForParentGoal(this, 1.0F, this.getMonsterParentClass());
		}
		this.goalSelector.removeGoal(this.followParentGoal);
		this.goalSelector.removeGoal(this.avoidPlayersGoal);
		if (!this.isTamed() && this.getMonsterParent() == null) {
			this.goalSelector.addGoal(4, this.avoidPlayersGoal);
			this.goalSelector.addGoal(3, this.followParentGoal);
		}
	}

	public EntityDimensions getDimensions(Pose pPose) {
		return POSES.getOrDefault(pPose, STANDING);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource pSource) {
		return (pSource.getEntity() instanceof BabyMonster baby && baby.getOwner() == this.getOwner())
				|| super.isInvulnerableTo(pSource);
	}

	@Nullable
	public ItemStack getPickResult() {
		return new ItemStack(Items.SPIDER_SPAWN_EGG);
	}

	@Override
	protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
		return pPose == Pose.SITTING ? 0.29F : 0.34F;
	}

	public boolean isFood(ItemStack pStack) {
		Item item = pStack.getItem();
		return item.isEdible() && pStack.getItem().getFoodProperties().isMeat();
	}

	@Override
	public LivingEntity getMonsterParent() {
		return this.animalParent;
	}

	@Override
	public void setMonsterParent(LivingEntity living) {
		this.animalParent = living;
	}

	@Override
	public Class<? extends PathfinderMob> getMonsterParentClass() {
		return Spider.class;
	}

	public boolean hurt(DamageSource pSource, float pAmount) {
		if (this.isInvulnerableTo(pSource)) {
			return false;
		}
		else {
			Entity entity = pSource.getEntity();
			if (!this.level.isClientSide) {
				this.setOrderedToSit(false);
			}
			if (entity != null && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
				pAmount = (pAmount + 1.0F) / 2.0F;
			}
			return super.hurt(pSource, pAmount);
		}
	}

	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (this.level.isClientSide) {
			boolean flag = this.isOwnedBy(pPlayer) || this.isTamed() || isFood(itemstack) && !this.isTamed()
					&& !this.isAngry();
			return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
		}
		else {
			if (this.isTamed()) {
				InteractionResult interactionresult = super.mobInteract(pPlayer, pHand);
				if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
					this.heal((float) itemstack.getItem().getFoodProperties().getNutrition());
					if (!pPlayer.getAbilities().instabuild) {
						itemstack.shrink(1);
					}
					this.gameEvent(GameEvent.EAT, this);
					this.playSound(SoundEvents.CAT_EAT);
					if (this.getHealth() == this.getMaxHealth()) { this.level.broadcastEntityEvent(this, (byte) 7); }
					return InteractionResult.SUCCESS;
				}
				else {
					if ((!interactionresult.consumesAction()) && this.isOwnedBy(pPlayer)) {
						this.setOrderedToSit(!this.isOrderedToSit());
						this.jumping = false;
						this.getNavigation().moveTo(this, 0);
						this.navigation.stop();
						this.setTarget(null);
						return InteractionResult.SUCCESS;
					}
				}
				return interactionresult;
			}
			else if (isFood(itemstack) && !this.isAngry() && this.canBeAdopted()) {
				if (!pPlayer.getAbilities().instabuild) {
					itemstack.shrink(1);
				}
				if (this.random.nextInt(3) == 0) {
					this.adopt(pPlayer);
					this.getNavigation().moveTo(this, 0);
					this.navigation.stop();
					this.setTarget(null);
					this.setOrderedToSit(true);
					this.level.broadcastEntityEvent(this, (byte) 7);
				}
				else {
					this.level.broadcastEntityEvent(this, (byte) 6);
				}
				return InteractionResult.SUCCESS;
			}
			return super.mobInteract(pPlayer, pHand);
		}
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_FLAGS_ID, (byte) 0);
		this.entityData.define(DATA_OWNERUUID_ID, Optional.empty());
	}

	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		this.addTamedSaveData(pCompound, this.orderedToSit);
		addPersistentAngerSaveData(pCompound);
	}

	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		readTamedSaveData(pCompound, this);
		orderedToSit = pCompound.getBoolean("Sitting");
		this.setInSittingPose(orderedToSit);
		readPersistentAngerSaveData(this.level, pCompound);
	}

	@Override
	public boolean canBeLeashed(Player pPlayer) {
		return !this.isLeashed() && pPlayer == this.getOwner();
	}

	public void handleEntityEvent(byte pId) {
		if (pId == 7) {
			this.spawnTamingParticles(true, this);
		}
		else if (pId == 6) {
			this.spawnTamingParticles(false, this);
		}
		else {
			super.handleEntityEvent(pId);
		}
	}

	@Override
	public boolean isTamed() {
		return (this.entityData.get(DATA_FLAGS_ID) & 4) != 0;
	}

	public void setTamed(boolean pTamed) {
		byte b0 = this.entityData.get(DATA_FLAGS_ID);
		if (pTamed) {
			this.entityData.set(DATA_FLAGS_ID, (byte) (b0 | 4));
		}
		else {
			this.entityData.set(DATA_FLAGS_ID, (byte) (b0 & -5));
		}
	}

	public boolean isInSittingPose() {
		return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
	}

	public void setInSittingPose(boolean pSitting) {
		byte b0 = this.entityData.get(DATA_FLAGS_ID);
		if (pSitting) {
			this.entityData.set(DATA_FLAGS_ID, (byte) (b0 | 1));
		}
		else {
			this.entityData.set(DATA_FLAGS_ID, (byte) (b0 & -2));
		}
	}

	@Override
	public void adopt(Player pPlayer) {
		this.setTamed(true);
		this.setParentUUID(pPlayer.getUUID());
		this.setMonsterParent(null);
		this.setPersistenceRequired();
		this.reassessTameGoals();
	}

	@Override
	public void tick() {
		if (this.getMonsterParent() != null && !this.getMonsterParent().isAlive()) {
			this.setMonsterParent(null);
			this.reassessTameGoals();
		}
		if (this.isInSittingPose()) {
			this.setPose(Pose.SITTING);
		}
		else {
			this.setPose(Pose.STANDING);
		}
		super.tick();
	}

	@Nullable
	public UUID getParentUUID() {
		return this.entityData.get(DATA_OWNERUUID_ID).orElse(null);
	}

	public void setParentUUID(@Nullable UUID pUuid) {
		this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(pUuid));
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

	public boolean canAttack(LivingEntity pTarget) {
		return !this.isOwnedBy(pTarget) && super.canAttack(pTarget);
	}

	public boolean isOwnedBy(LivingEntity pEntity) {
		return pEntity == this.getOwner();
	}

	@Override
	public boolean wantsToAttack(LivingEntity pTarget, LivingEntity pOwner) {
		return BabyMonster.super.wantsToAttack(pTarget, pOwner);
	}

	public Team getTeam() {
		if (this.isTamed()) {
			LivingEntity livingentity = this.getOwner();
			if (livingentity != null) {
				return livingentity.getTeam();
			}
		}
		return super.getTeam();
	}

	public boolean isAlliedTo(Entity pEntity) {
		if (this.isTamed()) {
			LivingEntity livingentity = this.getOwner();
			if (pEntity == livingentity) {
				return true;
			}
			if (livingentity != null) {
				return livingentity.isAlliedTo(pEntity);
			}
		}
		return super.isAlliedTo(pEntity);
	}

	public void die(DamageSource pCause) {
		net.minecraft.network.chat.Component deathMessage = this.getCombatTracker().getDeathMessage();
		super.die(pCause);
		if (this.dead) {
			if (!this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)
					&& this.getOwner() instanceof ServerPlayer) {
				if (this.getCombatTracker().getKiller() != this.getOwner() && this.getTarget() != null) {
					this.getOwner().sendSystemMessage(
							Component.translatable("death_msg." + this.getRandom().nextInt(5), this.getName(),
									this.getOwner().getName()));
				}
				else {
					this.getOwner().sendSystemMessage(deathMessage);
				}
			}
		}
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

	static class BabySpiderAttackGoal extends MeleeAttackGoal {
		private final BabySpider babySpider;

		public BabySpiderAttackGoal(BabySpider pSpider) {
			super(pSpider, 1.0D, true);
			this.babySpider = pSpider;
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
				return !this.babySpider.isOrderedToSit() && super.canContinueToUse();
			}
		}

		protected double getAttackReachSqr(LivingEntity pAttackTarget) {
			return 4.0F + pAttackTarget.getBbWidth();
		}
	}

	static class BabySpiderLeapAtTargetGoal extends LeapAtTargetGoal {
		private final BabySpider babySpider;

		public BabySpiderLeapAtTargetGoal(BabySpider pBabySpider, float pYd) {
			super(pBabySpider, pYd);
			this.babySpider = pBabySpider;
		}

		@Override
		public boolean canContinueToUse() {
			return !this.babySpider.isOrderedToSit() && super.canContinueToUse();
		}
	}
}
