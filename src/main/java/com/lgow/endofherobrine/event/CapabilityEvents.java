package com.lgow.endofherobrine.event;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.capability.CapabilityProvider;
import com.lgow.endofherobrine.util.ModResourceLocation;
import com.lgow.endofherobrine.world.data.ModSavedData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class CapabilityEvents {

	@SubscribeEvent
	public static void TESTEVENT(PlayerSleepInBedEvent event) {
		if (event.getEntity() instanceof ServerPlayer serverPlayer) {
			ModSavedData modData = ModSavedData.get(serverPlayer.getServer());
			if(modData.hasDefeatedHerobrine() && modData.getHerobrineRestTimer()>0){
				modData.setHerobrineRestTimer(0);
			}
			else if (modData.hasResurrectedHerobrine()) {
				modData.setDefeatedHerobrine(false);
				modData.setHerobrineRestTimer(0);
				modData.setHerobrineIsDead(false);
				modData.setResurrectedHerobrine(false);
			}
		}
	}
	@SubscribeEvent
	public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof Player) {
			event.addCapability(new ModResourceLocation("capabilities"), new CapabilityProvider());
		}
	}

	@SubscribeEvent
	public static void onJoinWorldSyncCap(EntityJoinLevelEvent event) {
		if (!event.getLevel().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer) {
			serverPlayer.getCapability(CapabilityProvider.WRATH).ifPresent(clan -> clan.syncValue(serverPlayer));
		}
	}

	@SubscribeEvent
	public static void onPlayerCloned(PlayerEvent.Clone event) {
		if (event.getEntity() instanceof ServerPlayer newPlayer
				&& event.getOriginal() instanceof ServerPlayer original) {
			event.getOriginal().reviveCaps();
			original.getCapability(CapabilityProvider.WRATH).ifPresent(
					oldCap -> newPlayer.getCapability(CapabilityProvider.WRATH)
							.ifPresent(newCap -> newCap.copyFrom(oldCap, newPlayer)));
		}
	}
}
