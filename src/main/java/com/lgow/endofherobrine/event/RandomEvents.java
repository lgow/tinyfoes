package com.lgow.endofherobrine.event;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.herobrine.Lurker;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lgow.endofherobrine.event.WrathHandler.probability;
import static com.lgow.endofherobrine.util.ModUtil.noHerobrineExists;
import static com.lgow.endofherobrine.util.ModUtil.spawnHerobrine;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class RandomEvents {
	private int deepMiningCooldown;

	//Attempts to spawn herobrine when a player mines a block below y 30
	@SubscribeEvent
	public void onDeepMining(BlockEvent.BreakEvent event) {
		Player player = event.getPlayer();
		Direction facing = player.getDirection();
		if (player.level() instanceof ServerLevel server && this.deepMiningCooldown <= 0 && player.getY() < 30
				&& noHerobrineExists(server) && probability(server, 0.001F)) {
			Lurker lurker = EntityInit.LURKER.get().create(server);
			spawnHerobrine(lurker, server, facing.getOpposite(), player.position(), 7);
			this.deepMiningCooldown = 120000;
			if (lurker.hasLineOfSight(player)) {
				player.sendSystemMessage(
						Component.translatable("whisper.behind").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
			}
		}
	}

	//Cooldown manager
	@SubscribeEvent
	public void serverTick(TickEvent.ServerTickEvent event) {
		if (deepMiningCooldown > 0) {
			--deepMiningCooldown;
		}
	}
}
