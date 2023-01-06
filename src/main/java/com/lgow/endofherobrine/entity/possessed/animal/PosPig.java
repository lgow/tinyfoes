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
import com.lgow.endofherobrine.entity.possessed.PosPigman;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PosPig extends Pig implements NeutralMob, PossessedMob {

    @Nullable
    private UUID persistentAngerTarget;
    private int remainingPersistentAngerTime;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(40, 80);

    public PosPig(EntityType<? extends PosPig> type, Level level) { super(type, level);}

    public static boolean checkPosAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor accessor, MobSpawnType spawnType, BlockPos pos, RandomSource rand) {
        return ModUtil.canSpawnPosCreature(accessor) && accessor.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 40)
                .add(Attributes.ATTACK_DAMAGE, 3);
    }

    @Override
    public MobType getMobType() { return ModMobTypes.POSSESSED;}

    @Override
    protected SoundEvent getAmbientSound() { return null;}

    @Override
    protected ResourceLocation getDefaultLootTable() { return new ResourceLocation("minecraft", "entities/pig");}

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.25,true));
        this.goalSelector.addGoal(1, new StarePlayerGoal(this, 40));
        this.goalSelector.addGoal(2, new FloatGoal(this));
        this.goalSelector.addGoal(3, new PosNeutralPanicGoal(this,1.25));
        this.goalSelector.addGoal(4, new TemptGoal(this, 20, Ingredient.of(Items.CARROT_ON_A_STICK), false));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new PosNeutralWalkAroundGoal(this, 1D));
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
            this.updatePossession(this, EntityType.PIG.create(level), this.isAngry());
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
    }

    @Override
    public void thunderHit(ServerLevel server, LightningBolt lightning) {
        PosPigman pigman = EntityInit.POS_PIGMAN.get().create(server);
        if (this.isSaddled()) {
            pigman.setItemInHand(InteractionHand.MAIN_HAND, Items.SADDLE.getDefaultInstance());
            pigman.setGuaranteedDrop(EquipmentSlot.MAINHAND);
        }
        this.convertTo(EntityInit.POS_PIGMAN.get(),true);
    }

    @Override
    protected void actuallyHurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player && amount < this.getHealth()) {
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
            lightningBolt.setPos(this.getX(), this.getY(), this.getZ());
            level.addFreshEntity(lightningBolt);
        }
        super.actuallyHurt(source, amount);
    }

    @Override
    public Pig getBreedOffspring(ServerLevel server, AgeableMob mob) { return EntityInit.POS_PIG.get().create(server);}

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
