package com.lgow.endofherobrine.entity.possessed;


import com.lgow.endofherobrine.ModUtil;
import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.ai.StarePlayerGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class PosZombie extends Zombie {

    public PosZombie(EntityType<? extends PosZombie> type, Level level) { super(type, level);}

    public static boolean checkPosMonsterSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor accessor, MobSpawnType spawnType, BlockPos pos, RandomSource rand) {
        return ModUtil.canSpawnPosMonster(accessor) && checkMonsterSpawnRules(type, accessor, spawnType, pos, rand);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 35)
                .add(Attributes.FOLLOW_RANGE, 40).add(Attributes.MOVEMENT_SPEED, 0.23F)
                .add(Attributes.ATTACK_DAMAGE, 6.0D).add(Attributes.ARMOR, 4.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    @Override
    public MobType getMobType() { return ModMobTypes.POSSESSED;}

    @Override
    protected boolean isSunSensitive() { return false;}

    @Override
    protected SoundEvent getAmbientSound() { return null;}

    @Override
    protected ResourceLocation getDefaultLootTable() { return new ResourceLocation("minecraft", "entities/zombie");}

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new ZombieAttackGoal(this, 1, true));
        this.goalSelector.addGoal(2, new StarePlayerGoal(this, 40));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pRandom, pDifficulty);
        if (pRandom.nextFloat() < (this.level.getDifficulty() == Difficulty.HARD ? 0.2F : 0.05F)) {
            int i = pRandom.nextInt(3);
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(i == 0 ? Items.IRON_SWORD : Items.IRON_SHOVEL));
        }
    }
}