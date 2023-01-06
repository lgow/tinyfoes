package com.lgow.endofherobrine.entity.possessed;

import com.lgow.endofherobrine.ModUtil;
import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.ai.StarePlayerGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class PosHusk extends Husk {

    public PosHusk(EntityType<? extends PosHusk> type, Level level) { super(type, level);}

    public static boolean checkPosMonsterSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor accessor, MobSpawnType spawnType, BlockPos pos, RandomSource rand) {
        return ModUtil.canSpawnPosMonster(accessor) && checkMonsterSpawnRules(type, accessor, spawnType, pos, rand);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 35)
                .add(Attributes.MOVEMENT_SPEED, 0.23F)
                .add(Attributes.FOLLOW_RANGE, 40)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ARMOR, 4.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    @Override
    public MobType getMobType() { return ModMobTypes.POSSESSED;}

    @Override
    protected SoundEvent getAmbientSound() { return null;}

    @Override
    protected ResourceLocation getDefaultLootTable() { return new ResourceLocation("minecraft", "entities/husk");}

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new ZombieAttackGoal(this, 1, true));
        this.goalSelector.addGoal(2, new StarePlayerGoal(this, 40));
        this.goalSelector.addGoal(6, new FloatGoal(this));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag && this.getMainHandItem().isEmpty() && entityIn instanceof LivingEntity) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            ((LivingEntity) entityIn).addEffect(new MobEffectInstance(MobEffects.HUNGER, 280 * (int) f));
        }
        return flag;
    }
}
