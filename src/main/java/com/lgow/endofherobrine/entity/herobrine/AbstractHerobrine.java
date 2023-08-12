package com.lgow.endofherobrine.entity.herobrine;

import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.Teleporter;
import com.lgow.endofherobrine.entity.ai.StarePlayerGoal;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public abstract class AbstractHerobrine extends PathfinderMob implements Teleporter {
	private static final EntityDataAccessor<Optional<UUID>> PLAYER_UUID = SynchedEntityData.defineId(
			AbstractHerobrine.class, EntityDataSerializers.OPTIONAL_UUID);
	protected int teleportCooldown;
	@Nullable private Player targetPlayer;

	protected AbstractHerobrine(EntityType<? extends PathfinderMob> type, Level level) {
		super(type, level);
		this.xpReward = 0;
		this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
	}

	public static AttributeSupplier.Builder setCustomAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20D).add(Attributes.MOVEMENT_SPEED, 0.6D)
				.add(Attributes.FOLLOW_RANGE, 256D).add(Attributes.ATTACK_DAMAGE, 0.1D);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
		this.spawnPositoning(blockPosition());
		this.setCustomName(Component.literal("Herobrine"));
		LogUtils.getLogger().info(this.getType().toShortString().toUpperCase() + " spawned at " + this.getOnPos());
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1F, true));
		this.goalSelector.addGoal(1, new StarePlayerGoal(this, Float.MAX_VALUE, true));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(PLAYER_UUID, Optional.empty());
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (hasPlayerUUID()) {
			tag.putUUID("PlayerUUID", this.getPlayerUUID());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		UUID uuid;
		if (tag.hasUUID("PlayerUUID")) {
			uuid = tag.getUUID("PlayerUUID");
		}
		else {
			String s = tag.getString("PlayerUUID");
			uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
		}
		if (uuid != null) {
			this.setPlayerUUID(uuid);
		}
	}

	@Override
	public boolean canBeCollidedWith() { return false; }

	@Override
	public boolean isInvulnerableTo(DamageSource pSource) {
		return !pSource.is(DamageTypes.OUTSIDE_BORDER);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) { return null; }

	@Override
	protected SoundEvent getDeathSound() { return null; }

	@Override
	public MobType getMobType() { return ModMobTypes.HEROBRINE; }

	@Override
	public boolean isPushable() { return false; }

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dim) { return 1.725F; }

	@Nullable
	public Player getTargetPlayer() { return this.targetPlayer; }

	public void setTargetPlayer(@Nullable Player pTarget) { this.targetPlayer = pTarget; }

	@Override
	public boolean hasLineOfSight(Entity pEntity) {
		if (pEntity.level() != this.level()) {
			return false;
		}
		else {
			Vec3 vec3 = new Vec3(this.getX(), this.getEyeY(), this.getZ());
			Vec3 vec31 = new Vec3(pEntity.getX(), pEntity.getEyeY(), pEntity.getZ());
			return this.level().clip(
					new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType()
					== HitResult.Type.MISS;
		}
	}

	@Override
	public int getMaxHeadYRot() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getMaxHeadXRot() {
		return Integer.MAX_VALUE;
	}

	@Override
	protected void customServerAiStep() {
		if(this.isInWater()){
			this.moveTo(position().add(0,1,0));
			this.setDeltaMovement(Vec3.ZERO);
		}
		this.setDeltaMovement(this.getDeltaMovement().multiply(1,0,1));
		super.customServerAiStep();
	}

	public boolean hasPlayerUUID() { return this.getPlayerUUID() != null; }

	public UUID getPlayerUUID() { return this.entityData.get(PLAYER_UUID).orElse(null); }

	public void setPlayerUUID(UUID uuid) { this.entityData.set(PLAYER_UUID, Optional.ofNullable(uuid)); }

	public Player getPlayerByUUID() { return this.level().getPlayerByUUID(getPlayerUUID()); }

	public boolean canSeeAnyPlayers() {
		if (this.level().isClientSide) {
			return false;
		}
		for (Player player : this.getServer().getPlayerList().getPlayers()) {
			if (this.hasLineOfSight(player)) {
				this.setTargetPlayer(player);
				return true;
			}
		}
		return false;
	}

	public void destroyBlocksInAABB(AABB area) {
		int minX = Mth.floor(area.minX);
		int minY = Mth.floor(area.minY);
		int minZ = Mth.floor(area.minZ);
		int maxX = Mth.floor(area.maxX);
		int maxY = Mth.floor(area.maxY);
		int maxZ = Mth.floor(area.maxZ);
		boolean destroyBlock = false;
		for (int x = minX; x <= maxX; ++x) {
			for (int y = minY; y <= maxY; ++y) {
				for (int z = minZ; z <= maxZ; ++z) {
					BlockPos blockpos = new BlockPos(x, y, z);
					BlockState blockstate = this.level().getBlockState(blockpos);
					if (!blockstate.isAir() && !blockstate.is(BlockTags.FIRE)) {
						destroyBlock = this.level().destroyBlock(blockpos, true);
					}
				}
			}
		}
		if (destroyBlock) {
			this.swing(InteractionHand.MAIN_HAND, true);
		}
	}

	protected boolean tpToWatchPlayer(Player player) {
		if (!this.level().isClientSide && this.isAlive() && player != null) {
			double randX = this.random.nextIntBetweenInclusive(15, 40);
			double randZ = this.random.nextIntBetweenInclusive(15, 40);
			double x = player.getX() + (this.random.nextBoolean() ? randX : -randX);
			double y = player.getY() + this.random.nextInt(16);
			double z = player.getZ() + (this.random.nextBoolean() ? randZ : -randZ);
			return this.attemptTeleport(this, x, y, z, player,
					!this.level().getBiome(player.blockPosition()).is(BiomeTags.IS_OCEAN));
		}
		return false;
	}

	public void spawnPositoning(BlockPos pos) {
		BlockState blockState = level().getBlockState(pos.above());
		boolean blocksMotion = blockState.blocksMotion();
		if (blocksMotion && level().canSeeSky(pos.above())) {
			this.moveTo(pos.above(), getXRot(), getYRot());
		}
		else if (isInWall()) {
			this.destroyBlocksInAABB(this.getBoundingBox());
		}
	}

	protected void teleportAway() {
		this.level().broadcastEntityEvent(this, (byte) 46);
		if (!this.isSilent()) {
			this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
		}
		this.discard();
	}

	public Player getNearestPlayer() {
		return this.level().getNearestPlayer(TargetingConditions.forNonCombat().ignoreLineOfSight(), this);
	}
}
