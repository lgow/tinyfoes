package net.tinyallies.entity;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;
import net.tinyallies.entity.ai.LookForParentGoal;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Zomby extends Zombie implements NeutralMob, BabyMonster {
	private static EntityDimensions STANDING = EntityDimensions.scalable(0.33F, 1.05F);
	private static final Map<Pose, EntityDimensions> BABY_POSES = ImmutableMap.<Pose, EntityDimensions> builder().put(
			Pose.STANDING, STANDING).put(Pose.CROUCHING, EntityDimensions.scalable(0.33F, 0.75F)).build();
	private final AvoidEntityGoal<Player> avoidPlayersGoal = new AvoidEntityGoal<>(this, Player.class, 16.0F, 0.8D,
			1.33D);
	private final LookForParentGoal followParentGoal = new LookForParentGoal(this, 1.0F, this.getParentClass());
	private final NearestAttackableTargetGoal<Player> targetPlayerGoal = new NearestAttackableTargetGoal<>(this, Player.class, true);
	protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Zomby.class, EntityDataSerializers.BYTE);
	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(Zomby.class,
			EntityDataSerializers.OPTIONAL_UUID);
	private boolean orderedToSit;
	private LivingEntity parent;

	public Zomby(EntityType<? extends Zombie> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.reassessTameGoals();
		applyAttributeModifiers();
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new ZombieAttackGoal(this, 1.0D, false));
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

	@Override
	public boolean isBaby() {
		return true;
	}

	@Override
	protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
		return pPose == Pose.CROUCHING ? 0.6F : 0.93F;
	}

	public EntityDimensions getDimensions(Pose pPose) {
		return BABY_POSES.getOrDefault(pPose, STANDING);
	}

	@Nullable
	public ItemStack getPickResult() {
		return new ItemStack(Items.ZOMBIE_SPAWN_EGG);
	}

@Override
	public boolean isInvulnerableTo(DamageSource pSource) {
		return this.hasSameOwner(pSource.getEntity()) || super.isInvulnerableTo(pSource);
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
		return Zombie.class;
	}

	public boolean hurt(DamageSource pSource, float pAmount) {
		return babyHurt(this, pSource, super.hurt(pSource, pAmount));
	}

	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		Item item = itemstack.getItem();
		if (!this.level.isClientSide && this.isTamed() && this.isOwnedBy(pPlayer)) {
			if (pPlayer.isCrouching()) {
				if (item instanceof ArmorItem || item instanceof SwordItem) {
					EquipmentSlot slot = getEquipmentSlotForItem(itemstack);
					if (!this.getItemBySlot(slot).isEmpty()) {
						this.spawnAtLocation(getItemBySlot(slot));
					}
					this.setItemSlotAndDropWhenKilled(slot, itemstack.copy());
					if (!pPlayer.getAbilities().instabuild) { pPlayer.getInventory().removeItem(itemstack); }
					this.playSound(slot.getType().equals(EquipmentSlot.Type.ARMOR) ? SoundEvents.ARMOR_EQUIP_GENERIC : SoundEvents.ITEM_FRAME_ADD_ITEM);
					return InteractionResult.SUCCESS;
				}
				else if (itemstack.isEmpty()) {
					this.getAllSlots().forEach(stack -> {
						this.spawnAtLocation(stack);
						this.setItemSlot(getEquipmentSlotForItem(stack), ItemStack.EMPTY);
					});
					return InteractionResult.SUCCESS;
				}
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
		readPersistentAngerSaveData(this.level, pCompound);
	}

	@Override
	public boolean canBeLeashed(Player pPlayer) {
		return !this.isLeashed() && pPlayer == this.getOwner();
	}

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

	@Override
	public Team getTeam() {
		return getBabyTeam(super.getTeam());
	}

	@Override
	public boolean isAlliedTo(Entity pEntity) {
		return babyIsAlliedTo(pEntity, super.isAlliedTo(pEntity));
	}

	@Override
	public void die(DamageSource pCause) {
		super.die(pCause);
		if (this.dead) { this.sendDeathMessage(this); }
	}

	@Override
	public boolean isPreventingPlayerRest(Player pPlayer) {
		return !this.isTamed();
	}

	@Override
	public boolean isOrderedToSit() {
		return this.orderedToSit;
	}

	@Override
	public void setOrderedToSit(boolean pOrderedToSit) {
		this.orderedToSit = pOrderedToSit;
	}

	@Override
	public boolean isUndead() {
		return true;
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
}