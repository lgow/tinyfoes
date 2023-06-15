package com.lgow.endofherobrine.event;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.config.ModConfigs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lgow.endofherobrine.event.WrathIncreaserEvents.probability;
import static com.lgow.endofherobrine.util.ModUtil.*;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class PossessionEvents {

	//Tries to possess mobs when spawning
	@SubscribeEvent
	public void onEntitySpawn(MobSpawnEvent event) {
		LivingEntity living = event.getEntity();
		if (ModConfigs.MOB_POSSESSION.get() && event.getLevel() instanceof ServerLevel server && living.tickCount == 1
				&& probability(server, 0.6F)) {
			possessMobs(living, server, false, true);
		}
	}

	//Tries to possess mobs when the player attacks them and increases the hurt timer
	@SubscribeEvent
	public void onMobHurt(LivingHurtEvent event) {
		LivingEntity target = event.getEntity();
		if (ModConfigs.MOB_POSSESSION.get() && target.level instanceof ServerLevel server && event.getAmount() < target.getHealth()) {
			target.lastHurtByPlayerTime = 600;
			if (probability(server, 0.5f)) {
				possessMobs(target, server, true, false);
			}
		}
	}

}
