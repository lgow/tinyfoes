package com.lgow.endofherobrine.event;

import com.lgow.endofherobrine.Main;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import static com.lgow.endofherobrine.enchantment.EnchantmentInit.BLESSING;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class WrathHandler {
	private static Score getScore(ServerLevel level, String player, String Objective) {
		Scoreboard scoreboard = level.getScoreboard();
		return scoreboard.getOrCreatePlayerScore(player, scoreboard.getObjective(Objective));
	}

	//returns a valid herobrine anger value
	public static int getHerobrineHostility(ServerLevel level) {
		return Mth.clamp(getScore(level, "Total", "DestructionDis").getScore() / 40, 0, 40);
	}

	//has a weighted chance of returning true
	static boolean probability(ServerLevel level, float weight) {
		return new Random().nextInt(100) <= getHerobrineHostility(level) * weight;
	}

	private void increaseDestruction(ServerPlayer player, int value) {
		getScore(player.getLevel(), player.getScoreboardName(), "Destruction").add(value);
	}

	//Increases world destruction each time a player breaks a block
	@SubscribeEvent
	public void onBlockBreakScore(BlockEvent.BreakEvent event) {
		if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
			this.increaseDestruction(serverPlayer, 1);
		}
	}

	//Increases world destruction each time a player places a block
	@SubscribeEvent
	public void onBlockPlaceScore(BlockEvent.EntityPlaceEvent event) {
		if (event.getEntity() instanceof ServerPlayer serverPlayer) {
			this.increaseDestruction(serverPlayer, 1);
		}
	}

	//Displays anger % on a scoreboard
	@SubscribeEvent
	public void onTick(TickEvent.LevelTickEvent event) {
		if (event.level instanceof ServerLevel server) {
			getScore(server, "Wrath", "DestructionDis").setScore(getHerobrineHostility(server));
		}
	}

	//Increases world destruction each time a player kills a mob
	@SubscribeEvent
	public void onMobKillScore(LivingDeathEvent event) {
		LivingEntity target = event.getEntity();
		Entity entity = target.lastHurtByPlayerTime <= 0 ? target.lastHurtByPlayer : event.getSource().getEntity();
		if (entity instanceof ServerPlayer player && !(player.getMainHandItem()
				.getEnchantmentLevel(BLESSING.get()) > 0)) {
			if (!target.getType().getCategory().isFriendly()) {
				increaseDestruction(player, 2);
			}
			else {
				increaseDestruction(player, 4);
			}
		}
	}
}
