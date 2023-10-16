package com.lgow.endofherobrine.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.Collections;

public class EndOfHerobrineCommand {

//	public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
//		pDispatcher.register(Commands.literal("endofherobrine").requires((r) -> r.hasPermission(0))
//				.then(Commands.literal("config").then(Commands.argument( "target", EntityArgument.players())
//						.executes((r) -> )))).then(Commands.literal("set")
//						.requires((r) -> r.hasPermission(2)).then(Commands.argument("target", EntityArgument.players())
//								.then(Commands.argument("clan", ClanArgument.clan()).executes(
//										(r) -> setClan(r.getSource(),
//												Collections.singleton(r.getSource().getPlayerOrException()),
//												ClanArgument.getClan(r, "clan")))))));
//	}
}
