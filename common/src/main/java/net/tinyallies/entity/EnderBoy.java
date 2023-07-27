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
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.tinyallies.entity.ai.LookForParentGoal;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class EnderBoy extends EnderMan implements NeutralMob, BabyMonster {
	protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(EnderBoy.class,
			EntityDataSerializers.BYTE);
	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(
			EnderBoy.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDimensions STANDING = EntityDimensions.scalable(0.33F, 1.4F);
	private static final Map<Pose, EntityDimensions> POSES = ImmutableMap.<Pose, EntityDimensions> builder().put(
			Pose.STANDING, STANDING).put(Pose.SITTING, EntityDimensions.scalable(0.33F, 0.75F)).build();
	private final AvoidEntityGoal<Player> avoidPlayersGoal = new AvoidEntityGoal<>(this, Player.class, 16.0F, 0.8D,
			1.33D);
	private final LookForParentGoal followParentGoal = new LookForParentGoal(this, 1.0F, this.getParentClass());
	private final NearestAttackableTargetGoal<Player> targetPlayerGoal = new NearestAttackableTargetGoal<>(this,
			Player.class, true);
	private boolean orderedToSit;
	private LivingEntity parent;

	public EnderBoy(EntityType<? extends EnderMan> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.reassessTameGoals();
		applyAttributeModifiers();
	}
	

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new EnderboyAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(1, new FleeRainGoal(this));
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

	@Nullable
	public ItemStack getPickResult() {
		return new ItemStack(Items.ENDERMAN_SPAWN_EGG);
	}

	@Override
	protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
		return pPose == Pose.SITTING ? 0.56F : 1.33F;
	}

	public EntityDimensions getDimensions(Pose pPose) {
		return POSES.getOrDefault(pPose, STANDING);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource pSource) {
		return this.hasSameOwner(pSource.getEntity()) || super.isInvulnerableTo(pSource);
	}

	public boolean isFood(ItemStack pStack) {
		return pStack.is(Items.CHORUS_FRUIT);
	}

	@Override
	public boolean isOrderedToSit() {
		return this.orderedToSit;
	}

	@Override
	public void setOrderedToSit(boolean b) {
		this.orderedToSit = b;
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
		return EnderMan.class;
	}

	public boolean hurt(DamageSource pSource, float pAmount) {
		return babyHurt(this, pSource, super.hurt(pSource, pAmount));
	}

	protected void customServerAiStep() {
		if (!this.isOrderedToSit()) {
			super.customServerAiStep();
		}
	}

	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (!this.level().isClientSide && this.isTamed() && this.isOwnedBy(pPlayer) && pPlayer.isCrouching()) {
			if (itemstack.isEmpty()) {
				this.spawnAtLocation(this.getCarriedBlock().getBlock());
				this.setCarriedBlock(null);
				return InteractionResult.SUCCESS;
			}
			else if (itemstack.getItem() instanceof BlockItem block) {
				if (this.getCarriedBlock() != null) {
					this.spawnAtLocation(this.getCarriedBlock().getBlock());
				}
				this.setCarriedBlock(block.getBlock().defaultBlockState());
				return InteractionResult.SUCCESS;
			}
		}
		return babyInteract(pPlayer, pHand, super.mobInteract(pPlayer, pHand));
	}

	@Override
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
		readPersistentAngerSaveData(this.level(), pCompound);
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
	protected boolean teleport() {
		return !this.isOrderedToSit() && super.teleport();
	}

	@Override
	public boolean isPreventingPlayerRest(Player pPlayer) {
		return !this.isTamed();
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

	public static class FleeRainGoal extends FleeSunGoal {
		private final Level level = this.mob.level();
		private final EnderBoy baby = (EnderBoy) this.mob;
		private double wantedX, wantedY, wantedZ;

		public FleeRainGoal(PathfinderMob pathfinderMob) {
			super(pathfinderMob, 1.0D);
		}

		@Override
		public boolean canUse() {
			if (!this.level.canSeeSky(this.mob.blockPosition())) {
				return false;
			}
			if (!this.level.isRaining()) {
				return false;
			}
			return this.setWantedPos();
		}

		protected boolean setWantedPos() {
			Vec3 vec3 = this.getHidePos();
			if (vec3 == null) {
				return false;
			}
			this.wantedX = vec3.x;
			this.wantedY = vec3.y;
			this.wantedZ = vec3.z;
			return true;
		}

		@Override
		public void start() {
			this.baby.teleportTo(this.wantedX,this.wantedY,this.wantedZ);
		}
	}

	public static class EnderboyAttackGoal extends MeleeAttackGoal{
		public EnderboyAttackGoal(PathfinderMob pathfinderMob, double d, boolean bl) {
			super(pathfinderMob, d, bl);
		}

		@Override
		public boolean canUse() {
			return !this.mob.level().isRaining() && super.canUse();
		}

		@Override
		public boolean canContinueToUse() {
			return !this.mob.level().isRaining() && super.canContinueToUse();
		}
	}
}
