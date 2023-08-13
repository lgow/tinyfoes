package net.tinyallies.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.scores.Team;
import net.tinyallies.entity.ai.FollowOwnerGoal;
import net.tinyallies.entity.ai.SitWhenOrderedToGoal;
import net.tinyallies.entity.ai.*;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static net.minecraft.core.particles.ParticleTypes.*;

public interface BabyMonster {
	AttributeModifier SPEED_MODIFIER = new AttributeModifier(
			UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836"), "Speed boost", 0.5,
			AttributeModifier.Operation.MULTIPLY_BASE);
	AttributeModifier DAMAGE_MODIFIER = new AttributeModifier(
			UUID.fromString("1eaf83ff-7207-4596-b37a-d7297b3ec4ce"), "Damage nerf", -0.25,
			AttributeModifier.Operation.MULTIPLY_BASE);
	@NotNull
	private PathfinderMob getAsMob() {
		return (PathfinderMob) this;
	}

	@NotNull
	private NeutralMob getAsNeutral() {
		return (NeutralMob) this;
	}

	//=============================================== DEFAULTS =======================================================//
	default void applyAttributeModifiers() {
		if(!(this instanceof Zomby)){
			AttributeInstance speedInstance = this.getAsMob().getAttribute(Attributes.MOVEMENT_SPEED);
			speedInstance.addTransientModifier(SPEED_MODIFIER);
		}
		AttributeInstance damageInstance = this.getAsMob().getAttribute(Attributes.ATTACK_DAMAGE);
		damageInstance.addTransientModifier(DAMAGE_MODIFIER);
	}
	default boolean isUndead() {
		return false;
	}

	default boolean canBeTamed() {
		return this.getParent() == null;
	}

	default LivingEntity getOwner() {
		UUID uuid = this.getOwnerUUID();
		return uuid == null ? null : this.getAsMob().level.getPlayerByUUID(uuid);
	}

	default boolean isOwnedBy(Entity pEntity) {
		return pEntity == this.getOwner();
	}

	default boolean hasSameOwner(Entity pEntity) {
		return pEntity instanceof BabyMonster baby && baby.getOwner() == this.getOwner();
	}

	default void tame(Player pPlayer) {
		this.setTamed(true);
		this.setOwnerUUID(pPlayer.getUUID());
		this.setParent(null);
		this.getAsMob().setPersistenceRequired();
		this.reassessTameGoals();
	}

	default void defaultBabyGoals(PathfinderMob entity) {
		if (this.isUndead()) {
			entity.goalSelector.addGoal(1, new BabyFleeSunGoal(entity, 1.0D));
			entity.goalSelector.addGoal(2, new RestrictSunGoal(entity));
		}
		else if (!(entity instanceof EnderBoy)) {
			entity.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(entity, 1.0D));
		}
		entity.goalSelector.addGoal(3, new FollowOwnerGoal(entity, 1.0D, 12.0F, 3.0F, false));
		entity.goalSelector.addGoal(4, new FloatGoal(entity));
		entity.goalSelector.addGoal(5, new SitWhenOrderedToGoal(entity));
		entity.goalSelector.addGoal(8, new LookAtPlayerGoal(entity, Player.class, 8F));
		entity.goalSelector.addGoal(9, new RandomLookAroundGoal(entity));
		entity.targetSelector.addGoal(0, new HelpOwnerTargetGoal(entity));
		entity.targetSelector.addGoal(1, new HurtByTargetGoal(entity));
		entity.targetSelector.addGoal(2, new DefendOwnerTargetGoal(entity));
	}

	default InteractionResult babyInteract(Player pPlayer, InteractionHand pHand, InteractionResult pSuper) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		PathfinderMob mob = this.getAsMob();
		NeutralMob neutral = this.getAsNeutral();
		if (mob.level.isClientSide) {
			boolean canInteract = this.isOwnedBy(pPlayer) || this.isTamed() || isFood(itemstack) && !this.isTamed()
					&& !neutral.isAngry();
			return canInteract ? InteractionResult.CONSUME : InteractionResult.PASS;
		}
		else {
			if (this.isTamed() && this.isOwnedBy(pPlayer)) {
				if (this.isFood(itemstack) && mob.getHealth() < mob.getMaxHealth()) {
					mob.heal((float) 4);
					if (!pPlayer.getAbilities().instabuild) {
						itemstack.shrink(1);
					}
					mob.gameEvent(GameEvent.EAT, mob);
					mob.playSound(SoundEvents.GOAT_EAT);
					if (mob.getHealth() == mob.getMaxHealth()) { mob.level.broadcastEntityEvent(mob, (byte) 7); }
					return InteractionResult.SUCCESS;
				}
				else {
					if ((!pSuper.consumesAction()) && this.isOwnedBy(pPlayer) && !pPlayer.isCrouching()) {
						this.setOrderedToSit(!this.isOrderedToSit());
						mob.setJumping(false);
						mob.getNavigation().moveTo(mob,0);
						mob.getNavigation().stop();
						neutral.setTarget(null);
						return InteractionResult.SUCCESS;
					}
				}
				return pSuper;
			}
			else if (isFood(itemstack) && !neutral.isAngry() && this.canBeTamed()) {
				if (!pPlayer.getAbilities().instabuild) {
					itemstack.shrink(1);
				}
				if (mob.getRandom().nextInt(3) == 0) {
					this.tame(pPlayer);
					mob.getNavigation().stop();
					neutral.setTarget(null);
					this.setOrderedToSit(true);
					mob.level.broadcastEntityEvent(mob, (byte) 7);
				}
				else {
					mob.level.broadcastEntityEvent(mob, (byte) 6);
				}
				return InteractionResult.SUCCESS;
			}
			return pSuper;
		}
	}

	default void addBabySaveData(CompoundTag pCompound, boolean orderedToSit) {
		if (this.getOwnerUUID() != null) {
			pCompound.putUUID("Parent", this.getOwnerUUID());
		}
		pCompound.putBoolean("Sitting", orderedToSit);
	}

	default void readBabySaveData(CompoundTag pCompound, LivingEntity entity) {
		UUID uuid = pCompound.hasUUID("Parent") ? pCompound.getUUID("Parent")
				: OldUsersConverter.convertMobOwnerIfNecessary(entity.getServer(), pCompound.getString("Parent"));
		if (uuid != null) {
			try {
				this.setOwnerUUID(uuid);
				this.setTamed(true);
			} catch (Throwable throwable) {
				this.setTamed(false);
			}
		}
	}

	default void spawnTamingParticles(boolean pTamed) {
		LivingEntity entity = this.getAsMob();
		ParticleOptions particleOptions = !pTamed ? SMOKE : this.isUndead() ? DAMAGE_INDICATOR : HEART;
		for (int i = 0; i < 7; ++i) {
			double d0 = entity.getRandom().nextGaussian() * 0.02D;
			double d1 = entity.getRandom().nextGaussian() * 0.02D;
			double d2 = entity.getRandom().nextGaussian() * 0.02D;
			entity.level.addParticle(particleOptions, entity.getRandomX(1.0D), entity.getRandomY() + 0.5D,
					entity.getRandomZ(1.0D), d0, d1, d2);
		}
	}

	default boolean babyWantsToAttack(LivingEntity pTarget, LivingEntity pOwner) {
		if (pTarget instanceof OwnableEntity) {
			return ((OwnableEntity) pTarget).getOwner() != pOwner;
		}
		else if (pTarget instanceof BabyMonster) {
			return !this.hasSameOwner(pTarget);
		}
		return true;
	}

	default void handleBabyEvent(byte pId) {
		if (pId == 7) {
			this.spawnTamingParticles(true);
		}
		else if (pId == 6) {
			this.spawnTamingParticles(false);
		}
	}

	default void updatePose(LivingEntity pTarget) {
		pTarget.setPose(this.isOrderedToSit() ? Pose.CROUCHING : Pose.STANDING);
	}

	default boolean babyHurt(LivingEntity livingEntity, DamageSource pSource, boolean b) {
		if (!livingEntity.isInvulnerableTo(pSource) && !livingEntity.level.isClientSide) {
			((BabyMonster) livingEntity).setOrderedToSit(false);
		}
		return b;
	}

	default void sendDeathMessage(PathfinderMob entity) {
		Component deathMessage = entity.getCombatTracker().getDeathMessage();
		if (!entity.level.isClientSide && entity.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)) {
			LivingEntity owner = ((BabyMonster) entity).getOwner();
			if (owner instanceof ServerPlayer serverPlayer) {
				if (entity.getKillCredit() != owner && entity.getTarget() != null) {
					int randomMessageIndex = entity.getRandom().nextInt(10);
					Component deathMessageCopy = deathMessage.copy().append(
							Component.translatable("death_msg." + randomMessageIndex, owner.getName()));
					serverPlayer.sendSystemMessage(deathMessageCopy);
				}
				else {
					serverPlayer.sendSystemMessage(deathMessage);
				}
			}
		}
	}

	default Team getBabyTeam(Team pSuper) {
		LivingEntity owner = this.getOwner();
		if (this.isTamed() && owner != null) {
			return owner.getTeam();
		}
		return pSuper;
	}

	default boolean babyIsAlliedTo(Entity pEntity, boolean pSuper) {
		if (this.isTamed()) {
			LivingEntity owner = this.getOwner();
			return owner != null && (pEntity == owner || owner.isAlliedTo(pEntity));
		}
		return pSuper;
	}

	//================================================ ABSTRACTS =====================================================//
	Class<? extends PathfinderMob> getParentClass();

	LivingEntity getParent();

	void setParent(LivingEntity living);

	boolean isFood(ItemStack itemstack);

	boolean isTamed();

	void setTamed(boolean b);

	void reassessTameGoals();

	UUID getOwnerUUID();

	void setOwnerUUID(UUID uuid);

	boolean isOrderedToSit();

	void setOrderedToSit(boolean b);

	boolean isInSittingPose();

	void setInSittingPose(boolean b);
}
