package com.lgow.endofherobrine.entity.possessed.animal;

import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.PossessedMob;
import com.lgow.endofherobrine.entity.ai.StarePlayerGoal;
import com.lgow.endofherobrine.entity.ai.neutral.PosNeutralPanicGoal;
import com.lgow.endofherobrine.entity.ai.neutral.PosNeutralWalkAroundGoal;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PosVillager extends AgeableMob implements VillagerDataHolder, NeutralMob, PossessedMob {
    @Nullable
    private UUID persistentAngerTarget;
    private int remainingPersistentAngerTime;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(40, 80);
    private static final EntityDataAccessor<VillagerData> DATA_VILLAGER_DATA = SynchedEntityData.defineId(PosVillager.class, EntityDataSerializers.VILLAGER_DATA);

    public PosVillager(EntityType<? extends PosVillager> type, Level level) {
        super(type, level);
        BuiltInRegistries.VILLAGER_PROFESSION.getRandom(this.random).ifPresent(
                (holder) -> this.setVillagerData(this.getVillagerData().setProfession(holder.value())));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 35)
                .add(Attributes.FOLLOW_RANGE, 40)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ARMOR, 4.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    @Override
    public MobType getMobType() { return ModMobTypes.POSSESSED;}

    @Override
    public SoundEvent getAmbientSound() { return null;}

    @Override
    public SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.VILLAGER_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return this.isBaby() ? 0.81F : 1.62F;
    }

    @Override
    protected ResourceLocation getDefaultLootTable() { return new ResourceLocation("minecraft", "entities/villager");}

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.5, true));
        this.goalSelector.addGoal(1, new StarePlayerGoal(this, 40));
        this.goalSelector.addGoal(2, new FloatGoal(this));
        this.goalSelector.addGoal(3, new PosNeutralPanicGoal(this,1.5));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new PosNeutralWalkAroundGoal(this, 1D));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, Monster.class).setAlertOthers());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VILLAGER_DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide){
            this.updatePossession(this, EntityType.VILLAGER.create(level), this.isAngry());
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
    }

    @Override
    protected void populateDefaultEquipmentEnchantments(RandomSource source, DifficultyInstance difficulty) {}

    @Override
    public PosVillager getBreedOffspring(ServerLevel server, AgeableMob mob) { return EntityInit.POS_VILLAGER.get().create(server);}

    @Override
    public void startPersistentAngerTimer() { this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));}

    @Override
    public int getRemainingPersistentAngerTime() { return this.remainingPersistentAngerTime;}

    @Override
    public void setRemainingPersistentAngerTime(int pTime) { this.remainingPersistentAngerTime = pTime;}

    @Nullable @Override
    public UUID getPersistentAngerTarget() { return this.persistentAngerTarget;}

    @Override
    public void setPersistentAngerTarget(@Nullable UUID pTarget) { this.persistentAngerTarget = pTarget;}

    @OnlyIn(Dist.CLIENT)
    public ArmPose getArmPose() {
        if (this.isAggressive()) {
            return ArmPose.ATTACK;
        } else {
            return ArmPose.IDLE;
        }
    }

    public void setVillagerData(VillagerData villagerData) {
        this.entityData.set(DATA_VILLAGER_DATA, villagerData);
    }

    public VillagerData getVillagerData() {
        return this.entityData.get(DATA_VILLAGER_DATA);
    }

    @javax.annotation.Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        this.setVillagerData(this.getVillagerData().setType(VillagerType.byBiome(pLevel.getBiome(this.blockPosition()))));
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    protected void addParticlesAroundSelf(ParticleOptions pParticleOption) {
        for(int i = 0; i < 5; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(pParticleOption, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    @Override
    protected void actuallyHurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player && amount < this.getHealth()) {
            this.level.broadcastEntityEvent(this, (byte)13);
        }
        super.actuallyHurt(source, amount);
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 13) {
            this.addParticlesAroundSelf(ParticleTypes.ANGRY_VILLAGER);
        }else {
            super.handleEntityEvent(pId);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public enum ArmPose {
        IDLE,
        ATTACK
    }
}
