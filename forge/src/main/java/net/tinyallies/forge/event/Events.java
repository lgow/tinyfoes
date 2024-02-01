package net.tinyallies.forge.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tinyallies.common.TinyFoesCommon;
import net.tinyallies.common.registry.ModEffects;
import net.tinyallies.forge.capabilities.CapabilityProvider;

@Mod.EventBusSubscriber(modid = TinyFoesCommon.MODID)
public class Events {
	@SubscribeEvent
	public static void convertAllBabyZombies(TickEvent.PlayerTickEvent event) {
		if (event.player instanceof ServerPlayer serverPlayer){
			serverPlayer.getCapability(CapabilityProvider.IS_BABYFIED_CAPABILITY).ifPresent((isBabyfied) -> {
				isBabyfied.setValue(serverPlayer.hasEffect(ModEffects.BABYFICATION.get()), serverPlayer);
			});
		}
	}
}