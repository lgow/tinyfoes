package net.tinyallies.entity;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.tinyallies.entity.ai.LookForParentGoal;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Creepy extends Creeper implements NeutralMob, BabyMonster {
	protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Creepy.class,
			EntityDataSerializers.BYTE);
	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(
			Creepy.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDimensions STANDING = EntityDimensions.scalable(0.33F, 0.85F);
	private static final Map<Pose, EntityDimensions> POSES = ImmutableMap.<Pose, EntityDimensions> builder().put(
			Pose.STANDING, STANDING).put(Pose.CROUCHING, EntityDimensions.scalable(0.33F, 0.75F)).build();
	private final AvoidEntityGoal<Player> avoidPlayersGoal = new AvoidEntityGoal<>(this, Player.class, 16.0F, 0.8D,
			1.33D);
	private final LookForParentGoal followParentGoal = new LookForParentGoal(this, 1.0F, this.getParentClass());
	private final NearestAttackableTargetGoal<Player> targetPlayerGoal = new NearestAttackableTargetGoal<>(this,
			Player.class, true);
	private boolean recentlyPopped, orderedToSit;
	private @Nullable LivingEntity parent;
	private int twinkleTime;

	public Creepy(EntityType<? extends Creeper> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.reassessTameGoals();
		applyAttributeModifiers();
	}

	public static float creepySeenPercent(Vec3 pExplosionVector, Entity pEntity) {
		AABB aabb = pEntity.getBoundingBox();
		double d0 = 1.0D / ((aabb.maxX - aabb.minX) * 2.0D + 1.0D);
		double d1 = 1.0D / ((aabb.maxY - aabb.minY) * 2.0D + 1.0D);
		double d2 = 1.0D / ((aabb.maxZ - aabb.minZ) * 2.0D + 1.0D);
		double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
		double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
		if (!(d0 < 0.0D) && !(d1 < 0.0D) && !(d2 < 0.0D)) {
			int i = 0;
			int j = 0;
			for (double d5 = 0.0D; d5 <= 1.0D; d5 += d0) {
				for (double d6 = 0.0D; d6 <= 1.0D; d6 += d1) {
					for (double d7 = 0.0D; d7 <= 1.0D; d7 += d2) {
						double d8 = Mth.lerp(d5, aabb.minX, aabb.maxX);
						double d9 = Mth.lerp(d6, aabb.minY, aabb.maxY);
						double d10 = Mth.lerp(d7, aabb.minZ, aabb.maxZ);
						Vec3 vec3 = new Vec3(d8 + d3, d9, d10 + d4);
						if (pEntity.level.clip(new ClipContext(vec3, pExplosionVector, ClipContext.Block.COLLIDER,
								ClipContext.Fluid.NONE, pEntity)).getType() == HitResult.Type.MISS) {
							++i;
						}
						++j;
					}
				}
			}
			return (float) i / (float) j;
		}
		else {
			return 0.0F;
		}
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(1, new BabySwellGoal(this));
		this.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0D, 1.2D));
		this.defaultBabyGoals(this);
	}

	public void reassessTameGoals() {
		this.goalSelector.removeGoal(this.followParentGoal);
		this.goalSelector.removeGoal(this.avoidPlayersGoal);
		this.goalSelector.removeGoal(this.targetPlayerGoal);
		if (!this.isTamed()) {
			if (this.getParent() == null) {
				this.goalSelector.addGoal(4, this.avoidPlayersGoal);
			}
			else {
				this.goalSelector.addGoal(0, this.followParentGoal);
				this.targetSelector.addGoal(3, this.targetPlayerGoal);
			}
		}
	}

	@Override
	public boolean isInvulnerableTo(DamageSource pSource) {
		return this.hasSameOwner(pSource.getEntity()) || super.isInvulnerableTo(pSource);
	}

	@Override
	protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
		return pPose == Pose.CROUCHING ? 0.68F : 0.8F;
	}

	@Override
	public EntityDimensions getDimensions(Pose pPose) {
		return POSES.getOrDefault(pPose, STANDING);
	}

	@Nullable
	public ItemStack getPickResult() {
		return new ItemStack(Items.CREEPER_SPAWN_EGG);
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
	public void explodeCreeper() {
		if (!this.level.isClientSide) {
			this.explode(this.getX(), this.getY(), this.getZ(), this.isPowered() ? 2.0F : 1.0F);
			this.finalizeExplosion();
			if (this.isPowered()) {
				this.discard();
			}
			else {
				this.hurt(DamageSource.GENERIC, 1);
			}
			this.spawnLingeringCloud();
			this.dead = true;
		}
	}

	public void explode(double x, double y, double z, float radius) {
		float f2 = radius * 2.0F;
		int k1 = Mth.floor(x - (double) f2 - 1.0D);
		int l1 = Mth.floor(x + (double) f2 + 1.0D);
		int i2 = Mth.floor(y - (double) f2 - 1.0D);
		int i1 = Mth.floor(y + (double) f2 + 1.0D);
		int j2 = Mth.floor(z - (double) f2 - 1.0D);
		int j1 = Mth.floor(z + (double) f2 + 1.0D);
		List<Entity> list = this.level.getEntities(this, new AABB(k1, i2, j2, l1, i1, j1));
		Vec3 vec3 = new Vec3(x, y, z);
		for (int k2 = 0; k2 < list.size(); ++k2) {
			Entity entity = list.get(k2);
			if (!entity.ignoreExplosion()) {
				double d12 = Math.sqrt(entity.distanceToSqr(vec3)) / (double) f2;
				if (d12 <= 1.0D) {
					double d5 = entity.getX() - x;
					double d7 = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - y;
					double d9 = entity.getZ() - z;
					double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
					if (d13 != 0.0D) {
						d5 /= d13;
						d7 /= d13;
						d9 /= d13;
						double d14 = creepySeenPercent(vec3, entity);
						double d10 = (1.0D - d12) * d14;
						double v = (d10 * d10 + d10) / 2.0D * 7.0D * f2 + 1.0D;
						if (!(entity instanceof LivingEntity living && babyWantsToAttack(living, this.getOwner()))) {
							entity.hurt(DamageSource.explosion(this), (float) ((int) v));
						}
						else {
							entity.hurt(DamageSource.explosion(this), (float) ((int) v));
						}
						double d11;
						if (entity instanceof LivingEntity livingentity) {
							d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener(livingentity, d10);
						}
						else {
							d11 = d10;
						}
						d5 *= d11;
						d7 *= d11;
						d9 *= d11;
						Vec3 vec31 = new Vec3(d5, d7, d9);
						entity.setDeltaMovement(entity.getDeltaMovement().add(vec31));
					}
				}
			}
		}
	}

	private void finalizeExplosion() {
		if (this.getRandom().nextInt(5) == 4) {
			this.playSound(SoundEvents.FIREWORK_ROCKET_TWINKLE);
			this.twinkleTime = 10;
		}
		else {
			this.playSound(this.random.nextBoolean() ? SoundEvents.FIREWORK_ROCKET_LARGE_BLAST
					: SoundEvents.FIREWORK_ROCKET_BLAST);
			this.level.broadcastEntityEvent(this, (byte) 101);
			this.twinkleTime = 2;
		}
	}

	public void setPowered(boolean b) {
		this.entityData.set(DATA_IS_POWERED, b);
	}

	@Override
	public boolean isFood(ItemStack itemstack) {
		return itemstack.is(Items.GUNPOWDER);
	}

	@Override
	public @Nullable LivingEntity getParent() {
		return this.parent;
	}

	@Override
	public void setParent(LivingEntity living) {
		this.parent = living;
	}

	@Override
	public Class<? extends PathfinderMob> getParentClass() {
		return Creeper.class;
	}

	public boolean hurt(DamageSource pSource, float pAmount) {
		this.setSwellDir(0);
		return babyHurt(this, pSource, super.hurt(pSource, pAmount));
	}

	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
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
		pCompound.putBoolean("Sitting", orderedToSit);
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
		if (pId == 101) {
			this.level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
		}
		else if (pId == 100) {
			for (int i = 0; i < this.getRandom().nextInt(5) + 2; ++i) {
				double d0 = this.getRandom().nextGaussian() * 0.05D;
				double d1 = this.getRandom().nextGaussian() * 0.05D;
				double d2 = this.getRandom().nextGaussian() * 0.05D;
				this.level.addParticle(ParticleTypes.CRIT, this.getRandomX(2.0D), this.getRandomY() + 0.5D,
						this.getRandomZ(2.0D), d0, d1, d2);
			}
		}
	}

	@Override
	public void tick() {
		if (this.getParent() != null && !this.getParent().isAlive()) {
			this.setParent(null);
			this.reassessTameGoals();
		}
		if (twinkleTime-- > 0) {
			this.level.broadcastEntityEvent(this, (byte) 100);
		}
		if (this.swell >= 30) {
			this.explodeCreeper();
			this.swell = 0;
		}
		this.updatePose(this);
		super.tick();
	}

	@Override
	public int getRemainingPersistentAngerTime() {
		return 0;
	}

	@Override
	public void setRemainingPersistentAngerTime(int pRemainingPersistentAngerTime) {
	}

	@Nullable
	@Override
	public UUID getPersistentAngerTarget() {
		return null;
	}

	@Override
	public void setPersistentAngerTarget(@Nullable UUID pPersistentAngerTarget) {
	}

	@Override
	public void startPersistentAngerTimer() {
	}

	public boolean canAttack(LivingEntity livingEntity) {
		return !this.hasSameOwner(livingEntity) && super.canAttack(livingEntity);
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

	static class BabySwellGoal extends Goal {
		private final Creepy creeper;
		@Nullable private LivingEntity target;

		public BabySwellGoal(Creepy pCreeper) {
			this.creeper = pCreeper;
		}

		public boolean canUse() {
			LivingEntity livingentity = this.creeper.getTarget();
			return this.creeper.getSwellDir() > 0 || livingentity != null && this.creeper.distanceToSqr(livingentity)
					< 4.0D && !this.creeper.recentlyPopped;
		}

		@Override
		public boolean canContinueToUse() {
			return this.target != null && this.target.isAlive() && this.creeper.distanceToSqr(this.target) < 4
					&& !this.creeper.recentlyPopped;
		}

		public void start() {
			this.target = this.creeper.getTarget();
		}

		public void stop() {
			this.target = null;
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		public void tick() {
			if (this.target == null) {
				this.creeper.setSwellDir(-1);
			}
			else if (this.creeper.distanceToSqr(this.target) > 49.0D) {
				this.creeper.setSwellDir(-1);
			}
			else if (!this.creeper.getSensing().hasLineOfSight(this.target)) {
				this.creeper.setSwellDir(-1);
			}
			else {
				this.creeper.setSwellDir(1);
			}
		}
	}
}
