package net.tinyallies.entity;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
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

public class BabyCreeper extends Creeper implements NeutralMob, BabyMonster {
	protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(BabyCreeper.class,
			EntityDataSerializers.BYTE);

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(
			BabyCreeper.class, EntityDataSerializers.OPTIONAL_UUID);

	protected static EntityDimensions STANDING = EntityDimensions.scalable(0.33F, 0.85F);

	private static final Map<Pose, EntityDimensions> POSES = ImmutableMap.<Pose, EntityDimensions> builder().put(
			Pose.STANDING, STANDING).put(Pose.SITTING, EntityDimensions.scalable(0.33F, 0.75F)).build();

	private boolean orderedToSit;

	private @Nullable LivingEntity animalParent;

	private AvoidEntityGoal<Player> avoidPlayersGoal;

	private LookForParentGoal followParentGoal;

	private int twinkletime;

	public BabyCreeper(EntityType<? extends Creeper> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.reassessTameGoals();
	}

	public static float getSeenPercent(Vec3 pExplosionVector, Entity pEntity) {
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
	public boolean isInvulnerableTo(DamageSource pSource) {
		return (pSource.getEntity() instanceof BabyMonster baby && baby.getOwner() == this.getOwner())
				|| super.isInvulnerableTo(pSource);
	}

	@Override
	protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
		return pPose == Pose.SITTING ? 0.68F : 0.8F;
	}

	@Override
	public EntityDimensions getDimensions(Pose pPose) {
		return POSES.getOrDefault(pPose, STANDING);
	}

	@Nullable
	public ItemStack getPickResult() {
		return new ItemStack(Items.CREEPER_SPAWN_EGG);
	}

//	@Override
//	public void explodeCreeper() {
//		if (!this.level.isClientSide) {
//			this.dead = true;
//			//			this.level.explode(this,this.getX(),this.getY(), this.getZ(), this.isPowered() ? 2.0F : 1.0F, Level.ExplosionInteraction.MOB );
//			this.explode(this.getX(), this.getY(), this.getZ(), this.isPowered() ? 2.0F : 1.0F);
//			this.finalizeExplosion();
//			if (this.isPowered()) { this.discard(); }
//			this.spawnLingeringCloud();
//		}
//	}

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
						double d14 = getSeenPercent(vec3, entity);
						double d10 = (1.0D - d12) * d14;
						double v = (d10 * d10 + d10) / 2.0D * 7.0D * f2 + 1.0D;
						if (!(entity instanceof LivingEntity living && wantsToAttack(living, this.getOwner()))) {
							entity.hurt(this.damageSources().explosion(null, this), (float) ((int) v));
						}
						else {
							entity.hurt(this.damageSources().explosion(null, this), (float) ((int) v));
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
		switch (this.getRandom().nextInt(5)) {
			case 4: {
				this.playSound(SoundEvents.FIREWORK_ROCKET_TWINKLE);
				this.twinkletime = 10;
			}
			default: {
				this.playSound(this.random.nextBoolean() ? SoundEvents.FIREWORK_ROCKET_LARGE_BLAST
						: SoundEvents.FIREWORK_ROCKET_BLAST);
				this.level.broadcastEntityEvent(this, (byte) 101);
			}
		}
	}

//	public void setPowered(boolean b) {
//		this.entityData.set(DATA_IS_POWERED, b);
//	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new BabySwellGoal(this));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
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

	public boolean isFood(ItemStack pStack) {
		return pStack.is(Items.GUNPOWDER);
	}

	@Override
	public @Nullable LivingEntity getMonsterParent() {
		return this.animalParent;
	}

	@Override
	public void setMonsterParent(LivingEntity living) {
		this.animalParent = living;
	}

	@Override
	public Class<? extends PathfinderMob> getMonsterParentClass() {
		return Creeper.class;
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
		Item item = itemstack.getItem();
		if (this.level.isClientSide) {
			boolean flag = this.isOwnedBy(pPlayer) || this.isTamed() || isFood(itemstack) && !this.isTamed()
					&& !this.isAngry();
			return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
		}
		else {
			if (this.isTamed()) {
				InteractionResult interactionresult = super.mobInteract(pPlayer, pHand);
				if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
					this.heal((float) 4);
					if (!pPlayer.getAbilities().instabuild) {
						itemstack.shrink(1);
					}
					this.gameEvent(GameEvent.EAT, this);
					this.playSound(SoundEvents.GOAT_EAT);
					if (this.getHealth() == this.getMaxHealth()) { this.level.broadcastEntityEvent(this, (byte) 7); }
					return InteractionResult.SUCCESS;
				}
				else {
					if ((!interactionresult.consumesAction()) && this.isOwnedBy(pPlayer)) {
						this.setOrderedToSit(!this.isOrderedToSit());
						this.jumping = false;
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
		if (this.orderedToSit) {
			this.setPose(Pose.SITTING);
		}
		this.setInSittingPose(orderedToSit);
		readPersistentAngerSaveData(this.level, pCompound);
	}

	@Override
	public boolean canBeLeashed(Player pPlayer) {
		return !this.isLeashed() && pPlayer == this.getOwner();
	}

	public void handleEntityEvent(byte pId) {
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
		else if (pId == 7) {
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
		if (this.isOrderedToSit()) {
			this.setPose(Pose.SITTING);
		}
		else {
			this.setPose(Pose.STANDING);
		}
//		if (this.swell >= this.maxSwell) {
//			this.swell = this.random.nextInt(10) + 5;
//			this.explodeCreeper();
//		}
		if (twinkletime-- > 0) {
			this.level.broadcastEntityEvent(this, (byte) 100);
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

	static class BabySwellGoal extends Goal {
		private final Creeper creeper;

		@Nullable private LivingEntity target;

		public BabySwellGoal(Creeper pCreeper) {
			this.creeper = pCreeper;
		}

		public boolean canUse() {
			LivingEntity livingentity = this.creeper.getTarget();
			return this.creeper.getSwellDir() > 0 || livingentity != null && this.creeper.distanceToSqr(livingentity)
					< 4.0D;
		}

		@Override
		public boolean canContinueToUse() {
			return this.target != null && this.target.isAlive() && this.creeper.distanceToSqr(this.target) < 4;
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
