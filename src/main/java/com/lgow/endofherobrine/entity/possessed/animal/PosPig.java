package com.lgow.endofherobrine.entity.possessed.animal;

import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.PossessedAnimal;
import com.lgow.endofherobrine.entity.possessed.PosPigman;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PosPig extends Pig implements NeutralMob, PossessedAnimal {
	private static final AttributeModifier SPEED_MODIFIER = new AttributeModifier(UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836"), "Pig speed boost", 10D, AttributeModifier.Operation.MULTIPLY_BASE);
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(40, 80);
	@Nullable private UUID persistentAngerTarget;
	private int remainingPersistentAngerTime;

	public PosPig(EntityType<? extends PosPig> type, Level level) { super(type, level); }

	public static AttributeSupplier.Builder setCustomAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10).add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.FOLLOW_RANGE, 40).add(Attributes.ATTACK_DAMAGE, 3);
	}

	@Override
	protected ResourceLocation getDefaultLootTable() { return new ResourceLocation("minecraft", "entities/pig"); }

	@Override
	protected void registerGoals() {
		this.registerPosAnimalGoals(this, 1.25D);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!this.level().isClientSide) {
			Pig pig = (Pig) this.convertBack(this, EntityType.PIG, !this.isAngry());
			if (this.isSaddled()) {
				pig.equipSaddle(null);
			}
			this.updatePersistentAnger((ServerLevel) this.level(), true);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		this.addPersistentAngerSaveData(pCompound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.readPersistentAngerSaveData(this.level(), pCompound);
	}

	@Override
	protected SoundEvent getAmbientSound() { return null; }

	@Override
	public void thunderHit(ServerLevel server, LightningBolt lightning) {
		PosPigman pigman = EntityInit.PIGMAN.get().create(server);
		if (this.isSaddled()) {
			pigman.setItemInHand(InteractionHand.MAIN_HAND, Items.SADDLE.getDefaultInstance());
			pigman.setGuaranteedDrop(EquipmentSlot.MAINHAND);
		}
		this.convertTo(EntityInit.PIGMAN.get(), true);
	}

	@Nullable
	public LivingEntity getControllingPassenger() {
		if (this.isSaddled()) {
			Entity entity = this.getFirstPassenger();
			if (entity instanceof Player) {
				Player player = (Player) entity;
				AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
				attributeinstance.removeModifier(SPEED_MODIFIER);
				this.setMaxUpStep(1.0F);
				if (player.getMainHandItem().is(Items.CARROT_ON_A_STICK) || player.getOffhandItem().is(
						Items.CARROT_ON_A_STICK)) {
					attributeinstance.addTransientModifier(SPEED_MODIFIER);
					this.setMaxUpStep(1.5F);
					return player;
				}
			}
		}
		return null;
	}

	@Override
	public Pig getBreedOffspring(ServerLevel server, AgeableMob mob) { return EntityInit.PIG.get().create(server); }

	@Override
	protected void actuallyHurt(DamageSource source, float amount) {
		if (source.getEntity() instanceof Player && amount < this.getHealth()) {
			LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level());
			lightningBolt.setPos(this.getX(), this.getY(), this.getZ());
			level().addFreshEntity(lightningBolt);
		}
		super.actuallyHurt(source, amount);
	}

	@Override
	public MobType getMobType() { return ModMobTypes.POSSESSED; }

	@Override
	public int getRemainingPersistentAngerTime() { return this.remainingPersistentAngerTime; }

	@Override
	public void setRemainingPersistentAngerTime(int pTime) { this.remainingPersistentAngerTime = pTime; }

	@Nullable
	@Override
	public UUID getPersistentAngerTarget() { return this.persistentAngerTarget; }

	@Override
	public void setPersistentAngerTarget(@Nullable UUID pTarget) { this.persistentAngerTarget = pTarget; }

	@Override
	public void startPersistentAngerTimer() {
		this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
	}
}
