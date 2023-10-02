package com.lgow.endofherobrine.event;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.config.ModConfigs;
import com.lgow.endofherobrine.entity.PossessedMob;
import com.lgow.endofherobrine.util.ModUtil;
import com.lgow.endofherobrine.world.data.ModSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lgow.endofherobrine.event.WrathHandler.probability;
import static com.lgow.endofherobrine.util.ModUtil.possessMob;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class PossessionEvents {
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
		if (event.getEntity().level() instanceof ServerLevel serverLevel) {
			if (event.getEntity() instanceof PossessedMob posMob) {
					ModUtil.revertPossession(((Mob) posMob), posMob.canRevertPossession());
			}
			else if (event.getEntity() instanceof Mob mob && ModSavedData.get(serverLevel.getServer())
					.hasResurrectedHerobrine()) {
				possessMob(mob, serverLevel, true, true);
			}
		}
	}
}
