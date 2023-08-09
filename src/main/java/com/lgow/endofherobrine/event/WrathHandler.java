package com.lgow.endofherobrine.event;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.capability.CapabilityProvider;
import com.lgow.endofherobrine.util.ModResourceLocation;
import com.lgow.endofherobrine.world.data.ModSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.atomic.AtomicInteger;

import static com.lgow.endofherobrine.enchantment.EnchantmentInit.BLESSING;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class WrathHandler {
	private static Score getScore(ServerLevel level, String player) {
		Scoreboard scoreboard = level.getScoreboard();
		return scoreboard.getOrCreatePlayerScore(player, scoreboard.getObjective("Destruction"));
	}

	private static int getTotalDestruction(ServerLevel level) {
		AtomicInteger totalWrath = new AtomicInteger();
		for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
			if (player.isAlive()) {
				player.getCapability(CapabilityProvider.WRATH).ifPresent(wrath -> {
					totalWrath.addAndGet(wrath.getValue());
				});
			}
		}
		return totalWrath.get();
	}

	//returns herobrine's wrath
	public static int getHerobrinesWrath(ServerLevel level) {
		ModSavedData data = ModSavedData.get(level.getServer());
		if (data.getHerobrineIsDeadOrResting()) {
			return 0;
		}
		int maxWrath = data.getResurrectedHerobrine() ? 100 : (data.getDefeatedHerobrine() ? 80 : 40);
		return Math.min(getTotalDestruction(level) / 40, maxWrath);
	}

	//has a weighted chance of returning true
	static boolean probability(ServerLevel level, float weight) {
		return RandomSource.create().nextInt(100) <= getHerobrinesWrath(level) * weight;
	}

	private void increasePlayerDestruction(ServerPlayer player, int value) {
		if (!ModSavedData.get(player.getServer()).getHerobrineIsDeadOrResting()) {
			player.getCapability(CapabilityProvider.WRATH).ifPresent(wrath -> wrath.addValue(value, player));
		}
	}

	//Increases world destruction each time a player breaks a block
	@SubscribeEvent
	public void onBlockBreakScore(BlockEvent.BreakEvent event) {
		if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
			this.increasePlayerDestruction(serverPlayer, 1);
		}
	}

	//Increases world destruction each time a player places a block
	@SubscribeEvent
	public void onBlockPlaceScore(BlockEvent.EntityPlaceEvent event) {
		if (event.getEntity() instanceof ServerPlayer serverPlayer) {
			this.increasePlayerDestruction(serverPlayer, 1);
		}
	}

	//Increases world destruction each time a player kills a mob
	@SubscribeEvent
	public void onMobKillScore(LivingDeathEvent event) {
		LivingEntity target = event.getEntity();
		Entity entity = target.lastHurtByPlayerTime <= 0 ? target.lastHurtByPlayer : event.getSource().getEntity();
		if (entity instanceof ServerPlayer player && !player.getMainHandItem().getAllEnchantments().containsKey(
				BLESSING.get())) {
			increasePlayerDestruction(player, target.getType().getCategory().isFriendly() ? 4 : 2);
		}
	}

	//Sets Total anger % on a scoreboard & grants player achievement
	@SubscribeEvent
	public void onTick(TickEvent.LevelTickEvent event) {
		if (event.level instanceof ServerLevel server) {
			for (ServerPlayer player : server.getServer().getPlayerList().getPlayers()) {
				Score score = getScore(server, player.getName().getString());
				if (player.isAlive()) {
					player.getCapability(CapabilityProvider.WRATH).ifPresent(wrath -> {
						score.setScore(wrath.getValue());
					});
				}
				else {
					score.setScore(0);
				}
				if (score.getScore() >= 1) {
					Advancement advancement = server.getServer().getAdvancements().getAdvancement(
							new ModResourceLocation("story/root"));
					for (String s : player.getAdvancements().getOrStartProgress(advancement).getRemainingCriteria()) {
						player.getAdvancements().award(advancement, s);
					}
				}
			}
			getScore(server, "Wrath %").setScore(getHerobrinesWrath(server));
			getScore(server, "Total").setScore(getTotalDestruction(server));
		}
	}

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		ModSavedData data = ModSavedData.get(event.getServer());
		if (data.getHerobrineRestTimer() > 0) {
			data.setHerobrineRestTimer(data.getHerobrineRestTimer() - 1);
		}
	}

	@SubscribeEvent
	public void onLoad(LevelEvent.Load event) {
		// team colors
		if (event.getLevel() instanceof ServerLevel server) {
			if (server.getScoreboard().getObjective("Destruction") == null) {
				server.getScoreboard().addObjective("Destruction", ObjectiveCriteria.DUMMY,
						Component.literal("Destruction").withStyle(ChatFormatting.AQUA),
						ObjectiveCriteria.RenderType.INTEGER);
			}
		}
	}
}
