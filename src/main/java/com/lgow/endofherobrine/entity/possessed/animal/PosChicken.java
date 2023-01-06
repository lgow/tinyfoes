package com.lgow.endofherobrine.entity.possessed.animal;

import com.lgow.endofherobrine.ModUtil;
import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.PossessedMob;
import com.lgow.endofherobrine.entity.Teleporter;
import com.lgow.endofherobrine.entity.ai.StarePlayerGoal;
import com.lgow.endofherobrine.entity.ai.neutral.DefendPassiveMobsGoal;
import com.lgow.endofherobrine.entity.ai.neutral.PosNeutralPanicGoal;
import com.lgow.endofherobrine.entity.ai.neutral.PosNeutralTargetGoal;
import com.lgow.endofherobrine.entity.ai.neutral.PosNeutralWalkAroundGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PosChicken extends Chicken implements NeutralMob, PossessedMob, Teleporter {

    private @Nullable UUID persistentAngerTarget;
    private int remainingPersistentAngerTime;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(40, 80);

    public PosChicken(EntityType<? extends PosChicken> type, Level level) {
        super(type, level);
    }

    public static boolean checkPosAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor accessor, MobSpawnType spawnType, BlockPos pos, RandomSource rand) {
        return ModUtil.canSpawnPosCreature(accessor) && accessor.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.ATTACK_DAMAGE, 1.0);
    }

    public MobType getMobType() {
        return ModMobTypes.POSSESSED;
    }


    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected ResourceLocation getDefaultLootTable() { return new ResourceLocation("minecraft", "entities/chicken");}

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.5, true));
        this.goalSelector.addGoal(1, new StarePlayerGoal(this, 40.0F));
        this.goalSelector.addGoal(2, new FloatGoal(this));
        this.goalSelector.addGoal(3, new PosNeutralPanicGoal(this, 1.25));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new PosNeutralWalkAroundGoal(this, 1.0));
        this.targetSelector.addGoal(0, new DefendPassiveMobsGoal(this));
        this.targetSelector.addGoal(1, new PosNeutralTargetGoal(this, Player.class));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Monster.class).setAlertOthers());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.addPersistentAngerSaveData(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.readPersistentAngerSaveData(this.level, pCompound);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide){
            this.updatePossession(this, EntityType.CHICKEN.create(level), this.isAngry());
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
    }

    protected void dodgeTeleport() {
        if (!this.level.isClientSide && this.isAlive()) {
            double x = this.getX() + (this.random.nextDouble() - 0.5) * 10.0;
            double y = this.getY() + (double)this.random.nextInt(12);
            double z = this.getZ() + (this.random.nextDouble() - 0.5) * 10.0;
            if (this.blockPosition().distToCenterSqr(x, y, z) > 3.0) {
                this.attemptTeleport(this, x, y, z);
            }
        }
    }

    public boolean hurt(DamageSource source, float amount) {
        if (!this.level.isClientSide && source.getEntity() instanceof Player && amount < this.getHealth()) {
            this.dodgeTeleport();
        }
        return super.hurt(source, amount);
    }

    public Chicken getBreedOffspring(ServerLevel server, AgeableMob mob) { return EntityInit.POS_CHICKEN.get().create(server);
    }

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
}