package com.lgow.endofherobrine.entity.possessed.animal;

import com.lgow.endofherobrine.ModUtil;
import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.PossessedMob;
import com.lgow.endofherobrine.entity.ai.*;
import com.lgow.endofherobrine.entity.ai.neutral.DefendPassiveMobsGoal;
import com.lgow.endofherobrine.entity.ai.neutral.PosNeutralPanicGoal;
import com.lgow.endofherobrine.entity.ai.neutral.PosNeutralTargetGoal;
import com.lgow.endofherobrine.entity.ai.neutral.PosNeutralWalkAroundGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class PosSheep extends Sheep implements NeutralMob, PossessedMob {

    @Nullable
    private UUID persistentAngerTarget;
    private int remainingPersistentAngerTime;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(40, 80);
    private static final EntityDataAccessor<Integer> NUM = SynchedEntityData.defineId(PosSheep.class, EntityDataSerializers.INT);

    public PosSheep(EntityType<? extends PosSheep> type, Level level) { super(type, level);}

    public static boolean checkPosAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor accessor, MobSpawnType spawnType, BlockPos pos, RandomSource rand) {
        return ModUtil.canSpawnPosCreature(accessor) && accessor.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8D)
                .add(Attributes.MOVEMENT_SPEED, 0.23F)
                .add(Attributes.FOLLOW_RANGE, 40)
                .add(Attributes.ATTACK_DAMAGE, 3D);
    }

    public int getNum() { return this.entityData.get(NUM);}

    public void setNum(int num) { this.entityData.set(NUM, num);}

    @Override
    public MobType getMobType() { return ModMobTypes.POSSESSED;}

    @Override
    protected SoundEvent getAmbientSound() { return null;}

    @Override
    public ResourceLocation getDefaultLootTable() { return new ResourceLocation("minecraft", "entities/sheep");}

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.25, true));
        this.goalSelector.addGoal(1, new StarePlayerGoal(this, 40));
        this.goalSelector.addGoal(2, new FloatGoal(this));
        this.goalSelector.addGoal(3, new PosNeutralPanicGoal(this,1.25));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new PosNeutralWalkAroundGoal(this, 1D));
        this.targetSelector.addGoal(0, new DefendPassiveMobsGoal(this));
        this.targetSelector.addGoal(1, new PosNeutralTargetGoal<>(this, Player.class));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Monster.class).setAlertOthers());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(NUM, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Num", this.getNum());
        this.addPersistentAngerSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setNum(tag.getInt("Num"));
        this.readPersistentAngerSaveData(this.level, tag);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide){
            this.updatePossession(this, EntityType.SHEEP.create(level), this.isAngry() && this.getNum()!=0);
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
    }

    @Override
    protected void customServerAiStep() {}

    @Override
    public @NotNull List<ItemStack> onSheared(@Nullable Player player, @NotNull ItemStack item, Level level, BlockPos pos, int fortune) {
        this.setLastHurtByMob(player);
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
        return super.onSheared(player, item, level, pos, fortune);
    }

    @Override
    protected boolean shouldDropLoot() { return super.shouldDropLoot() && this.getNum() == 0;}

    @Override
    public boolean shouldDropExperience() { return super.shouldDropExperience() && this.getNum() == 0;}

    private void createSheep() {
        PosSheep sheep = EntityInit.POS_SHEEP.get().create(level);
        sheep.setColor(this.getColor());
        sheep.setSheared(this.isSheared());
        sheep.setBaby(this.isBaby());
        sheep.setCustomName(this.getCustomName());
        sheep.copyPosition(this);
        sheep.setNum(this.getNum() + 1);
        level.addFreshEntity(sheep);
    }

    @Override
    public void dropAllDeathLoot(DamageSource source) {
        if (this.getNum() <= 3 && (source.getEntity() instanceof Player || this.lastHurtByPlayer !=null)) {
            for (int i = 0; i < 2; i++) {
                createSheep();
            }
        }
        super.dropAllDeathLoot(source);
    }

    @Override
    public Sheep getBreedOffspring(ServerLevel server, AgeableMob mob) { return EntityInit.POS_SHEEP.get().create(server);}

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
