package net.tinyallies.forge.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tinyallies.common.TinyFoesCommon;
import net.tinyallies.common.util.TinyFoesResLoc;
import net.tinyallies.forge.capabilities.CapabilityProvider;

import static net.tinyallies.forge.capabilities.CapabilityProvider.IS_BABYFIED_CAPABILITY;
import static net.tinyallies.forge.capabilities.CapabilityProvider.IS_BABY_CAPABILITY;

@Mod.EventBusSubscriber(modid = TinyFoesCommon.MODID)
public class CapabilityEvents {
	@SubscribeEvent
	public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof Player) {
			event.addCapability(new TinyFoesResLoc("tiny_capabilities"), new CapabilityProvider() );
		}
	}

	@SubscribeEvent
	public static void onJoinWorldSyncCap(EntityJoinLevelEvent event) {
		if (!event.getLevel().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer) {
			serverPlayer.getCapability(IS_BABY_CAPABILITY).ifPresent(isBaby -> isBaby.syncValue(serverPlayer));
			serverPlayer.getCapability(IS_BABYFIED_CAPABILITY).ifPresent(isBabyfied -> isBabyfied.syncValue(serverPlayer));
		}
	}

	@SubscribeEvent
	public static void onPlayerCloned(PlayerEvent.Clone event) {
		if (event.getEntity() instanceof ServerPlayer newPlayer
				&& event.getOriginal() instanceof ServerPlayer original) {
			event.getOriginal().reviveCaps();
			//INFO
			original.getCapability(IS_BABY_CAPABILITY).ifPresent(oldCap -> newPlayer.getCapability(IS_BABY_CAPABILITY)
					.ifPresent(newCap -> newCap.copyFrom(oldCap, newPlayer)));
			original.getCapability(IS_BABYFIED_CAPABILITY).ifPresent(
					oldCap -> newPlayer.getCapability(IS_BABYFIED_CAPABILITY).ifPresent(newCap -> newCap.copyFrom(oldCap, newPlayer)));
		}
	}
}