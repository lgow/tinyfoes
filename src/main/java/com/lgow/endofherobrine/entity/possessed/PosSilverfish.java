package com.lgow.endofherobrine.entity.possessed;

import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.Teleporter;
import com.lgow.endofherobrine.entity.ai.SummonPosSilverfishGoal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class PosSilverfish extends Silverfish implements Teleporter {

    private SummonPosSilverfishGoal summonPosSilverfish;

    public PosSilverfish(EntityType<? extends PosSilverfish> type, Level level) { super(type, level);}

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 40D)
                .add(Attributes.ATTACK_DAMAGE, 1D);
    }

    @Override
    public MobType getMobType() { return ModMobTypes.POSSESSED;}

    @Override
    protected SoundEvent getAmbientSound() { return null;}

    @Override
    protected ResourceLocation getDefaultLootTable() { return new ResourceLocation("minecraft", "entities/silverfish");}


    @Override
    protected void registerGoals() {
        this.summonPosSilverfish = new SummonPosSilverfishGoal(this);
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, this.summonPosSilverfish);
        this.goalSelector.addGoal(6, new FloatGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this)).setAlertOthers());
    }

    protected boolean chechKTeleportPos(Player player) {
        if (!player.level.isClientSide && player.isAlive()) {
            double d0 = player.getX() + (this.random.nextInt(800) - 400.5);
            double d1 = player.getY() + (this.random.nextInt(64) + 12D);
            double d2 = player.getZ() + (this.random.nextInt(800) - 400.5);
            return this.attemptTeleport(player, d0, d1, d2);
        } else {
            return false;
        }
    }

    @Override
    public void onAddedToWorld() {
        List<PosSilverfish> list = this.level.getEntitiesOfClass(PosSilverfish.class, this.getBoundingBox().inflate(30));
        if(summonPosSilverfish!=null && list.size() == 1) { this.summonPosSilverfish.notifyHurt();}
        super.onAddedToWorld();
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hasTpPos;
        if (target instanceof Player player) {
            do{
                hasTpPos = this.chechKTeleportPos(player);
            }while(!hasTpPos);
        }
        return super.doHurtTarget(target);
    }


    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(summonPosSilverfish!=null) { this.summonPosSilverfish.notifyHurt();}
        return super.hurt(source, amount);
    }

    @Override
    protected void dropAllDeathLoot(DamageSource source) {
        if(summonPosSilverfish!=null) { this.summonPosSilverfish.notifyHurt();}
        super.dropAllDeathLoot(source);
    }
}



