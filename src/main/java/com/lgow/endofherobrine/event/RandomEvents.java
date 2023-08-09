package com.lgow.endofherobrine.event;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.herobrine.AbstractHerobrine;
import com.lgow.endofherobrine.entity.herobrine.Lurker;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lgow.endofherobrine.event.WrathHandler.probability;
import static com.lgow.endofherobrine.util.ModUtil.noHerobrineExists;
import static com.lgow.endofherobrine.util.ModUtil.spawnHerobrine;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class RandomEvents {
	private int deepMiningTimer, randomEventsTimer;

	//Attempts to spawn herobrine when a player mines a block below y 30
	@SubscribeEvent
	public void onDeepMining(BlockEvent.BreakEvent event) {
		Player player = event.getPlayer();
		Direction facing = player.getDirection();
		if (player.level() instanceof ServerLevel server && this.deepMiningTimer <= 0) {
			this.deepMiningTimer = 12000;
			if (player.getY() < 30 && noHerobrineExists(server) && probability(server, 0.001F)) {
				Lurker lurker = EntityInit.LURKER.get().create(server);
				spawnHerobrine(lurker, server, facing.getOpposite(), player.position(), 7);
				if (lurker.hasLineOfSight(player)) {
					player.sendSystemMessage(Component.translatable("whisper.behind")
							.withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
				}
			}
		}
	}

	@SubscribeEvent
	public void onRandomEvents(TickEvent.PlayerTickEvent event) {
		//todo player thrown up, surrounded by mobs, nausea effect
		Player player = event.player;
		if (this.randomEventsTimer <= 0) {
			this.randomEventsTimer = 600;
			if (player.level() instanceof ServerLevel server && probability(server, 0.05F)) {
				if (player.isCrouching() && !noHerobrineExists(player.level())) {
					player.hurt(player.damageSources().mobAttack(player.level()
									.getEntitiesOfClass(AbstractHerobrine.class, player.getBoundingBox().inflate(256)).get(0)),
							1F);
				}
			}
		}
	}
	@SubscribeEvent
	public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		BlockState state = event.getLevel().getBlockState(event.getPos());
		Block block = state.getBlock();
		if (block instanceof AbstractFurnaceBlock|| block instanceof AbstractChestBlock<?>) {
			this.randomEventsTimer = 600;
			event.getLevel().setBlockAndUpdate(event.getPos(), state.rotate(event.getLevel(), event.getPos(), Rotation.CLOCKWISE_180));
		}
	}

	//Timer manager
	@SubscribeEvent
	public void serverTick(TickEvent.ServerTickEvent event) {
		if (deepMiningTimer > 0) {
			--deepMiningTimer;
		}
		if (randomEventsTimer > 0) {
			--randomEventsTimer;
		}
	}
}
