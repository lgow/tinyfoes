package com.lgow.endofherobrine.entity.herobrine.boss;

import com.lgow.endofherobrine.entity.herobrine.AbstractHerobrine;
import com.lgow.endofherobrine.item.ItemInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class HerobrineBoss extends AbstractHerobrine implements RangedAttackMob {
	private static final EntityDataAccessor<Integer> DATA_ID_INV = SynchedEntityData.defineId(HerobrineBoss.class,
			EntityDataSerializers.INT);

	private static final EntityDataAccessor<Boolean> IS_ENRAGED = SynchedEntityData.defineId(HerobrineBoss.class,
			EntityDataSerializers.BOOLEAN);

	private static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = (livingEntity) ->
			livingEntity.getMobType() != MobType.UNDEAD && livingEntity.attackable();

	private final ServerBossEvent bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(),
			BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

	public HerobrineBoss(EntityType<? extends HerobrineBoss> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.moveControl = new FlyingMoveControl(this, 10, false);
		this.setHealth(this.getMaxHealth());
		this.setInvulnerable(false);
		this.xpReward = 50;
	}

	@Deprecated
	public static boolean canDestroy(BlockState pState) {
		return ! pState.isAir() && ! pState.is(BlockTags.WITHER_IMMUNE);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource pSource) {
		return !(pSource.getEntity() instanceof Player);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.6F)
				.add(Attributes.FLYING_SPEED, 0.6F).add(Attributes.FOLLOW_RANGE, 40.0D).add(Attributes.ARMOR, 4.0D);
	}

	@Override
	protected PathNavigation createNavigation(Level pLevel) {
		FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
		flyingpathnavigation.setCanOpenDoors(true);
		flyingpathnavigation.setCanFloat(true);
		flyingpathnavigation.setCanPassDoors(true);
		return flyingpathnavigation;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_ID_INV, 0);
		this.entityData.define(IS_ENRAGED, false);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putInt("Invul", this.getInvulnerableTicks());
		pCompound.putBoolean("IsEnraged", this.getEnraged());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.setInvulnerableTicks(pCompound.getInt("Invul"));
		this.setEnraged(pCompound.getBoolean("IsEnraged"));
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
		if (!this.getEnraged()) {
			super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
		} else{
			ItemEntity itementity = this.spawnAtLocation(ItemInit.HEROBRINE_HEAD_ITEM.get());
			if (itementity != null) {
				itementity.setExtendedLifetime();
			}
		}
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new ChargeUpGoal());
		this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 40, 20.0F));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2,
				new NearestAttackableTargetGoal<>(this, Player.class, 0, false, false, LIVING_ENTITY_SELECTOR));
	}

	@Override
	public void customServerAiStep() {
		if (this.getInvulnerableTicks() > 0) {
			int k1 = this.getInvulnerableTicks() - 1;
			this.bossEvent.setProgress(1.0F - (float) k1 / 220.0F);
			if (k1 <= 0) {
				this.level.explode(this, this.getX(), this.getY(), this.getZ(), 7f, Level.ExplosionInteraction.MOB);
				if (! this.isSilent()) {
					this.level.globalLevelEvent(1023, this.blockPosition(), 0);
				}
			}
			this.setInvulnerableTicks(k1);
			if (this.tickCount % 10 == 0) {
				this.heal(10.0F);
			}
		}
		else {
			super.customServerAiStep();
			if (this.tickCount % 20 == 0) {
				this.heal(1.0F);
			}
			this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
		}
	}

	private boolean getEnraged() { return this.entityData.get(IS_ENRAGED); }

	public void setEnraged(boolean isEnraged) {
		this.entityData.set(IS_ENRAGED, isEnraged);
	}

	@Override
	public void aiStep() {
		Vec3 vec3 = this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D);
		this.setDeltaMovement(vec3);
		if (vec3.horizontalDistanceSqr() > 0.05D) {
			this.setYRot((float) Mth.atan2(vec3.z, vec3.x) * (180F / (float) Math.PI) - 90.0F);
		}
		super.aiStep();
		for (int l = 0; l < 3; ++ l) {
			double d8 = this.getHeadX(l);
			double d10 = this.getHeadY(l);
			double d2 = this.getHeadZ(l);
			this.level.addParticle(ParticleTypes.SMOKE, d8 + this.random.nextGaussian() * (double) 0.3F,
					d10 + this.random.nextGaussian() * (double) 0.3F, d2 + this.random.nextGaussian() * (double) 0.3F,
					0.0D, 0.0D, 0.0D);
		}
		if (this.getInvulnerableTicks() > 0) {
			for (int i1 = 0; i1 < 3; ++ i1) {
				this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + this.random.nextGaussian(),
						this.getY() + (double) (this.random.nextFloat() * 3.3F),
						this.getZ() + this.random.nextGaussian(), 0.7F, 0.7F, 0.9F);
			}
		}
	}

	public void makeInvulnerable() {
		this.setInvulnerableTicks(220);
		this.bossEvent.setProgress(0.0F);
		this.setHealth(this.getMaxHealth() / 3.0F);
	}

	private double getHeadX(int pHead) {
		if (pHead <= 0) {
			return this.getX();
		}
		else {
			float f = (this.yBodyRot + (float) (180 * (pHead - 1))) * ((float) Math.PI / 180F);
			float f1 = Mth.cos(f);
			return this.getX() + (double) f1 * 1.3D;
		}
	}

	private double getHeadY(int pHead) {
		return pHead <= 0 ? this.getY() + 3.0D : this.getY() + 2.2D;
	}

	private double getHeadZ(int pHead) {
		if (pHead <= 0) {
			return this.getZ();
		}
		else {
			float f = (this.yBodyRot + (float) (180 * (pHead - 1))) * ((float) Math.PI / 180F);
			float f1 = Mth.sin(f);
			return this.getZ() + (double) f1 * 1.3D;
		}
	}

	private void performRangedAttack(int pHead, LivingEntity pTarget) {
		this.performRangedAttack(pHead, pTarget.getX(), pTarget.getY() + (double) pTarget.getEyeHeight() * 0.5D,
				pTarget.getZ(), pHead == 0 && this.random.nextFloat() < 0.001F);
	}

	private void performRangedAttack(int pHead, double pX, double pY, double pZ, boolean pIsDangerous) {
		if (! this.isSilent()) {
			this.level.levelEvent(null, 1024, this.blockPosition(), 0);
		}
		double d0 = this.getHeadX(pHead);
		double d1 = this.getHeadY(pHead);
		double d2 = this.getHeadZ(pHead);
		double d3 = pX - d0;
		double d4 = pY - d1;
		double d5 = pZ - d2;
		WitherSkull witherskull = new WitherSkull(this.level, this, d3, d4, d5);
		witherskull.setOwner(this);
		if (pIsDangerous) {
			witherskull.setDangerous(true);
		}
		witherskull.setPosRaw(d0, d1, d2);
		this.level.addFreshEntity(witherskull);
	}

	@Override
	public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
		this.performRangedAttack(0, pTarget);
	}

	@Override
	public boolean addEffect(MobEffectInstance pEffectInstance, @Nullable Entity pEntity) {
		return false;
	}

	@Override
	public boolean hurt(DamageSource pSource, float pAmount) {
		if (this.isInvulnerableTo(pSource)) {
			return false;
		}
		else if (!pSource.is(DamageTypeTags.IS_DROWNING) && ! (pSource.getEntity() instanceof HerobrineBoss)) {
			if (this.getInvulnerableTicks() > 0 && !pSource.is(DamageTypes.OUT_OF_WORLD)) {
				return false;
			}
			else {
				Entity entity1 = pSource.getEntity();
				if (! (entity1 instanceof Player) && entity1 instanceof LivingEntity
						&& ((LivingEntity) entity1).getMobType() == this.getMobType()) {
					return false;
				}
				return super.hurt(pSource, pAmount);
			}
		}
		return false;
	}

	@Override
	public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
		return false;
	}

	public int getInvulnerableTicks() {
		return this.entityData.get(DATA_ID_INV);
	}

	public void setInvulnerableTicks(int pInvulnerableTicks) {
		this.entityData.set(DATA_ID_INV, pInvulnerableTicks);
	}

	@Override
	protected boolean canRide(Entity pEntity) {
		return false;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public void setCustomName(@Nullable Component pName) {
		super.setCustomName(pName);
		this.bossEvent.setName(this.getDisplayName());
	}

	@Override
	public void startSeenByPlayer(ServerPlayer pPlayer) {
		super.startSeenByPlayer(pPlayer);
		this.bossEvent.addPlayer(pPlayer);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer pPlayer) {
		super.stopSeenByPlayer(pPlayer);
		this.bossEvent.removePlayer(pPlayer);
	}

	class ChargeUpGoal extends Goal {
		public ChargeUpGoal() {
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
		}

		public boolean canUse() {
			return HerobrineBoss.this.getInvulnerableTicks() > 0;
		}
	}
}
