package com.lgow.endofherobrine.event;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.config.ModConfigs;
import com.lgow.endofherobrine.entity.PossessedMob;
import com.lgow.endofherobrine.util.ModUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lgow.endofherobrine.event.WrathHandler.probability;
import static com.lgow.endofherobrine.util.ModUtil.possessMob;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class PossessionEvents {
	//Tries to possess mobs when spawning
	@SubscribeEvent
	public void onEntitySpawn(MobSpawnEvent event) {
		Mob mob = event.getEntity();
		if (ModConfigs.DO_MOB_POSSESSION.get() && event.getLevel() instanceof ServerLevel server && mob.tickCount == 1
				&& probability(server, 0.6F)) {
			possessMob(mob, server, false, true);
		}
	}

	//Tries to possess mobs when the player attacks them and increases the hurt timer
	@SubscribeEvent
	public void onMobHurt(LivingHurtEvent event) {
		if (event.getEntity() instanceof Mob mob && mob.level() instanceof ServerLevel server
				&& event.getAmount() < mob.getHealth()) {
			mob.lastHurtByPlayerTime = 600;
			if (probability(server, 0.5f)) {
				possessMob(mob, server, true, false);
			}
		}
	}

	@SubscribeEvent
	public void onMobTick(LivingEvent.LivingTickEvent event) {
		if (event.getEntity() instanceof PossessedMob posMob && ((Mob) posMob).level() instanceof ServerLevel) {
			if(!ModConfigs.DO_MOB_POSSESSION.get()) {
				ModUtil.revertPossession(((Mob) posMob), posMob.canRevertPossession());
			}
		}
	}
}
