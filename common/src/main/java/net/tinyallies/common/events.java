package net.tinyallies.common;

import dev.architectury.event.events.common.TickEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class events {
//	private static Score getScore(ServerLevel level, String player) {
//		Scoreboard scoreboard = level.getScoreboard();
//		return scoreboard.getOrCreatePlayerScore(player, scoreboard.getObjective("Wrath"));
//	}
//
//	{
//
//	}
//
//	//Sets Total anger % on a scoreboard & grants player achievement
//	@SubscribeEvent
//	public void onTickScoreboards(TickEvent.LevelTickEvent event) {
//
//	}
//
//	@SubscribeEvent
//	public void wrathAdvancement(TickEvent.PlayerTickEvent event) {
//		if (event.player instanceof ServerPlayer serverPlayer) {
//			serverPlayer.getCapability(CapabilityProvider.WRATH).ifPresent(wrath -> {
//				if (wrath.getValue() >= 1) {
//					AdvancementHolder advancement = serverPlayer.getServer().getAdvancements().get(
//							new ModResourceLocation("story/root"));
//					for (String s : serverPlayer.getAdvancements().getOrStartProgress(advancement)
//							.getRemainingCriteria()) {
//						serverPlayer.getAdvancements().award(advancement, s);
//					}
//				}
//			});
//		}
//	}
//
//	@SubscribeEvent
//	public void decreaseHerobrineRestTimer(TickEvent.ServerTickEvent event) {
//		ModSavedData data = ModSavedData.get(event.getServer());
//		if (data.getHerobrineRestTimer() > 0) {
//			data.setHerobrineRestTimer(data.getHerobrineRestTimer() - 1);
//		}
//	}
//
//	@SubscribeEvent
//	public void reformatScoreboardObjective(LevelEvent.Load event) {
//		if (event.getLevel() instanceof ServerLevel server) {
//			if (server.getScoreboard().getObjective("Wrath") == null) {
//				server.getScoreboard().addObjective("Wrath", ObjectiveCriteria.DUMMY,
//						Component.literal("Wrath").withStyle(ChatFormatting.AQUA),
//						ObjectiveCriteria.RenderType.INTEGER);
//			}
//			else {
//				server.getScoreboard().getObjective("Wrath").setDisplayName(
//						Component.literal("Wrath").withStyle(ChatFormatting.AQUA));
//			}
//			if (server.getScoreboard().getPlayerTeam("Wrath %") == null) {
//				PlayerTeam wrathTeam = server.getScoreboard().addPlayerTeam("Wrath %");
//				wrathTeam.setColor(ChatFormatting.RED);
//				wrathTeam.getPlayers().add("Wrath %");
//			}
//			if (server.getScoreboard().getPlayerTeam("Total") == null) {
//				PlayerTeam totalTeam = server.getScoreboard().addPlayerTeam("Total");
//				totalTeam.setColor(ChatFormatting.YELLOW);
//				totalTeam.getPlayers().add("Total");
//			}
//		}
//	}

}
