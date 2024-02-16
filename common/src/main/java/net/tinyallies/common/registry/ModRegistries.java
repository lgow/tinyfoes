package net.tinyallies.common.registry;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.tinyallies.common.entity.ModEntities;
import net.tinyallies.common.items.ModItems;

public class ModRegistries {

	public static void register() {
		ModEffects.register();
		ModEntities.register();
		ModItems.register();
//		LifecycleEvent.SERVER_LEVEL_LOAD.register((serverLevel)->{
//
//			if (serverLevel.getScoreboard().getObjective("IsBabyfied") == null) {
//				serverLevel.getScoreboard().addObjective("IsBabyfied", ObjectiveCriteria.DUMMY,
//						Component.literal("IsBabyfied").withStyle(ChatFormatting.AQUA), ObjectiveCriteria.RenderType.INTEGER);
//			}
//
//			if (serverLevel.getScoreboard().getObjective("IsBaby") == null) {
//				serverLevel.getScoreboard().addObjective("IsBaby", ObjectiveCriteria.DUMMY,
//						Component.literal("IsBaby").withStyle(ChatFormatting.AQUA), ObjectiveCriteria.RenderType.INTEGER);
//			}
//		});
	}

}
