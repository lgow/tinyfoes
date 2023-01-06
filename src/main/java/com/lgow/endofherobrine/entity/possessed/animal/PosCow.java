package com.lgow.endofherobrine.entity.possessed.animal;

import com.lgow.endofherobrine.ModUtil;
import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.PossessedMob;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static com.lgow.endofherobrine.entity.EntityInit.POS_COW;

public class PosCow extends Cow implements NeutralMob, PossessedMob {

    private @Nullable UUID persistentAngerTarget;
    private int remainingPersistentAngerTime;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(40, 80);


    public PosCow(EntityType<? extends PosCow> type, Level level) { super(type, level);}

    public static boolean checkPosAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor accessor, MobSpawnType spawnType, BlockPos pos, RandomSource rand) {
        return ModUtil.canSpawnPosCreature(accessor) && accessor.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FOLLOW_RANGE, 40)
                .add(Attributes.ATTACK_DAMAGE, 10);
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        this.setLastHurtByMob(pPlayer);
        return super.mobInteract(pPlayer, pHand);
    }

    @Override
    public MobType getMobType() { return ModMobTypes.POSSESSED;}

    @Override
    protected SoundEvent getAmbientSound() { return null;}

    @Override
    protected ResourceLocation getDefaultLootTable() { return new ResourceLocation("minecraft", "entities/cow");}

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 2D, true));
        this.goalSelector.addGoal(1, new StarePlayerGoal(this, 40F));
        this.goalSelector.addGoal(2, new FloatGoal(this));
        this.goalSelector.addGoal(3, new PosNeutralPanicGoal(this, 2D));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new PosNeutralWalkAroundGoal(this, 1D));
        this.targetSelector.addGoal(0, new DefendPassiveMobsGoal(this));
        this.targetSelector.addGoal(1, new PosNeutralTargetGoal<>(this, Player.class));
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
            this.updatePossession(this, EntityType.COW.create(level), this.isAngry());
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
    }

    @Override
    public void dropAllDeathLoot(DamageSource cause) {
        super.dropAllDeathLoot(cause);
        if (cause != DamageSource.OUT_OF_WORLD) {
            level.setBlockAndUpdate(this.blockPosition(), Blocks.LAVA.defaultBlockState());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
        this.updatePossession(this, POS_COW.get().create(level),true);
    }

    @Override
    public Cow getBreedOffspring(ServerLevel server, AgeableMob mob) {
        return EntityInit.POS_COW.get().create(server);
    }

    //NeutralMob Overrides
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
