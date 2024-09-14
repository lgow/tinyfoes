package net.tinyfoes.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Slime;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.tinyfoes.common.entity.BabyfiableEntity;
import net.tinyfoes.common.registry.ModEffects;

import java.util.Collection;
import java.util.List;

import static net.tinyfoes.common.config.TinyFoesConfigs.*;

public class ModCommads {
	private static final List<ModConfigSpec.ConfigValue<?>> serverList = List.of(BABY_MAX_HEALTH_MODIFIER, BABY_SPEED_MODIFIER,
			SPAWN_AS_BABY_ODDS, WITCH_GOAL);
	private static final List<ModConfigSpec.ConfigValue<?>> clientList = List.of(OVERSIZED_ITEMS, VILLAGER_HEAD_FIX);

	public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
		pDispatcher.register(Commands.literal("tinyfoes").requires((r) -> r.hasPermission(3))//
				.then(Commands.literal("server")//
						.then(Commands.literal("witchBabyficationGoal")
								.then(Commands.argument("value", BoolArgumentType.bool())//
										.executes((r) -> setConfigBoolean(r.getSource(), WITCH_GOAL,
												BoolArgumentType.getBool(r, "value")))))//
						.then(Commands.literal("spawnAsBabyOdds")//
								.then(Commands.argument("value", DoubleArgumentType.doubleArg(0))//
										.executes((r) -> setConfigDouble(r.getSource(), SPAWN_AS_BABY_ODDS,
												DoubleArgumentType.getDouble(r, "value")))))//
						.then(Commands.literal("babySpeedModifier")
								.then(Commands.argument("value", DoubleArgumentType.doubleArg(0))//
										.executes((r) -> setConfigDouble(r.getSource(), BABY_SPEED_MODIFIER,
												DoubleArgumentType.getDouble(r, "value")))))
						.then(Commands.literal("babyHealthModifier")
								.then(Commands.argument("value", DoubleArgumentType.doubleArg(0))//
										.executes((r) -> setConfigDouble(r.getSource(), BABY_MAX_HEALTH_MODIFIER,
												DoubleArgumentType.getDouble(r, "value")))))//
						.then(Commands.literal("resetAll")//
								.executes((r) -> resetAll(r.getSource(), serverList))))//
				.then(Commands.literal("client")//
						.then(Commands.literal("villagerHeadFix")
								.then(Commands.argument("value", BoolArgumentType.bool())//
										.executes((r) -> setConfigBoolean(r.getSource(), VILLAGER_HEAD_FIX,
												BoolArgumentType.getBool(r, "value")))))//
						.then(Commands.literal("oversizedItems")
								.then(Commands.argument("value", BoolArgumentType.bool())//
										.executes((r) -> setConfigBoolean(r.getSource(), OVERSIZED_ITEMS,
												BoolArgumentType.getBool(r, "value")))))//
						.executes((r) -> resetAll(r.getSource(), clientList))//
				));
		pDispatcher.register(Commands.literal("babyfy").requires((r) -> r.hasPermission(3))//
				.then(Commands.argument("entities", EntityArgument.entities())
						.then(Commands.argument("b", BoolArgumentType.bool())//
								.executes((r) -> babyfyEntity(r.getSource(), EntityArgument.getEntities(r, "entities"),
										BoolArgumentType.getBool(r, "b")))))//
		);
	}

	private static int babyfyEntity(CommandSourceStack source, Collection<? extends Entity> entities, boolean b) {
		for (Entity entity : entities) {
			if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(ModEffects.BABYFICATION)) {
				livingEntity.removeEffect(ModEffects.BABYFICATION);
			}
			else if (entity instanceof Slime slime) {
				slime.addEffect(new MobEffectInstance(ModEffects.BABYFICATION, 0, -1, false, false));
			}
			else if (entity instanceof Mob mob) {
				mob.setBaby(b);
			}
			else if (entity instanceof BabyfiableEntity babyfiableEntity) {
				babyfiableEntity.tinyfoes$$setBaby(b);
			}
		}
		if (entities.size() == 1) {
			source.sendSuccess(
					() -> Component.translatable("command.babyfy.single", entities.iterator().next().getDisplayName()),
					true);
		}
		else {
			source.sendSuccess(() -> Component.translatable("command.babyfy.multiple", entities.size()), true);
		}
		return 0;
	}

	private static int setConfigDouble(CommandSourceStack source, ModConfigSpec.ConfigValue<Double> s, double value) {
		s.set(value);
		source.sendSuccess(() -> Component.translatable("command.config.set", s.getPath().get(1), value), true);
		return 1;
	}

	private static int setConfigBoolean(CommandSourceStack source, ModConfigSpec.BooleanValue s, boolean value) {
		s.set(value);
		source.sendSuccess(() -> Component.translatable("command.config.set", s.getPath().get(1), value), true);
		return 1;
	}

	private static int resetAll(CommandSourceStack source, List<ModConfigSpec.ConfigValue<?>> configValueList) {
		for (ModConfigSpec.ConfigValue configValue : configValueList) {
			configValue.set(configValue.getDefault());
		}
		source.sendSuccess(() -> Component.translatable("command.config.reset",
				configValueList == clientList ? "client" : "server"), true);
		return 1;
	}
}
