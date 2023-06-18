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
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
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

	public Player getNearestPlayer() {
		return this.level().getNearestPlayer(TargetingConditions.forNonCombat().ignoreLineOfSight(), this);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(PLAYER_UUID, Optional.empty());
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if(hasPlayerUUID()){
			tag.putUUID("PlayerUUID", this.getPlayerUUID());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		UUID uuid;
		if (tag.hasUUID("PlayerUUID")) {
			uuid = tag.getUUID("PlayerUUID");
		} else {
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

	public boolean hasPlayerUUID() { return this.getPlayerUUID() != null; }

	public UUID getPlayerUUID() { return this.entityData.get(PLAYER_UUID).orElse(null); }

	public void setPlayerUUID(UUID uuid) { this.entityData.set(PLAYER_UUID, Optional.ofNullable(uuid)); }

	public Player getPlayerByUUID() { return this.level().getPlayerByUUID(getPlayerUUID()); }

	@Nullable
	public Player getTargetPlayer() { return this.targetPlayer; }

	public void setTargetPlayer(@Nullable Player pTarget) { this.targetPlayer = pTarget; }


	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1F, true));
		this.goalSelector.addGoal(1, new StarePlayerGoal(this, Float.MAX_VALUE));
		this.goalSelector.addGoal(2, new FloatGoal(this));
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
		this.spawnPositoning(blockPosition());
		this.setCustomName(Component.literal("Herobrine"));
		LogUtils.getLogger().info(this.getType().toShortString().toUpperCase() + " spawned at " + this.getOnPos());
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}

	public boolean isLookingAt(Player pPlayer) {
		Vec3 vec3 = this.getViewVector(1.0F).normalize();
		Vec3 vec31 = new Vec3(pPlayer.getX() - this.getX(), pPlayer.getEyeY() - this.getEyeY(),
				pPlayer.getZ() - this.getZ());
		double d0 = vec31.length();
		vec31 = vec31.normalize();
		double d1 = vec3.dot(vec31);
		return d1 > 1.0D - 0.025D / d0 && pPlayer.hasLineOfSight(this);
	}

	public boolean isLookingAtAnyPlayers() {
		boolean isLookinAtAnyPlayers = false;
		if (!this.level().isClientSide) {
			for (Player player : this.level().getServer().getPlayerList().getPlayers()) {
				isLookinAtAnyPlayers = isLookinAtAnyPlayers || this.isLookingAt(player);
				if (this.isLookingAt(player)) { this.setTargetPlayer(player); }
			}
		}
		return isLookinAtAnyPlayers;
	}

	public boolean canSeePlayers() {
		boolean canSeeplayers = false;
		if (!this.level().isClientSide) {
			for (Player player : this.level().getServer().getPlayerList().getPlayers()) {
				canSeeplayers = canSeeplayers || this.hasLineOfSight(player);
			}
		}
		return canSeeplayers;
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

	protected boolean tpToWatchPlayer(Player player) {
		if (!this.level().isClientSide && this.isAlive() && player != null) {
			double randX = this.random.nextIntBetweenInclusive(15, 40);
			double randZ = this.random.nextIntBetweenInclusive(15, 40);
			double x = player.getX() + (this.random.nextBoolean() ? randX : -randX);
			double y = player.getY() + this.random.nextInt(16);
			double z = player.getZ() + (this.random.nextBoolean() ? randZ : -randZ);
			return this.attemptTeleport(this, x, y, z, player,
					!player.level().getBiome(player.getOnPos()).is(BiomeTags.IS_OCEAN));
		}
		return false;
	}
}
