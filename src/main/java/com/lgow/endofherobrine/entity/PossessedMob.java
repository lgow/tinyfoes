package com.lgow.endofherobrine.entity;

import com.lgow.endofherobrine.ModUtil;
import com.lgow.endofherobrine.config.ModConfigs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public interface PossessedMob {

    default void updatePossession(LivingEntity possessed, LivingEntity regular, boolean canConvert) {
        if (ModConfigs.CONVERT_BACK.get() && possessed.tickCount > ModConfigs.REMAIN_POSSESSED.get() && !canConvert ) {
            ModUtil.convertEntity(possessed, (Mob) regular, possessed.getLevel() ,false);
        }
    }
}
