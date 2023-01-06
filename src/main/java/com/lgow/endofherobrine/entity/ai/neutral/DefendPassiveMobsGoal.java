package com.lgow.endofherobrine.entity.ai.neutral;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

public class DefendPassiveMobsGoal extends TargetGoal {

    private final Mob goalOwner;
    private LivingEntity aggressorTarget;

    private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(40.0D);

    public DefendPassiveMobsGoal(Mob goalOwner) {
        super(goalOwner, false, true);
        this.goalOwner = goalOwner;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        //Should defend from player interaction?
        AABB aabb = this.goalOwner.getBoundingBox().inflate(40.0D);
        List<Mob> list = this.goalOwner.level.getNearbyEntities(Mob.class, this.attackTargeting, this.goalOwner, aabb);
        List<ServerPlayer> list1 = this.goalOwner.level.getServer().getPlayerList().getPlayers();
        for(LivingEntity livingentity : list) {
            for(Player player : list1) {
                if(!(livingentity instanceof Enemy) && livingentity.lastHurtByPlayer == player) {
                    this.aggressorTarget = player;
                }
            }
        }
        if (this.aggressorTarget == null) {
            return false;
        } else {
            return !(this.aggressorTarget instanceof Player) || !this.aggressorTarget.isSpectator()
                    && !((Player)this.aggressorTarget).isCreative();
        }
    }

    public void start() {
        this.goalOwner.setTarget(this.aggressorTarget);
        super.start();
    }
}