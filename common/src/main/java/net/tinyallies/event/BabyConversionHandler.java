package net.tinyallies.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.tinyallies.entity.BabyMonster;
import net.tinyallies.util.ModUtil;

import java.util.Random;

public class BabyConversionHandler implements EntityEvent.Add {
	@Override
	public EventResult add(Entity entity, Level world) {
		if (!world.isClientSide) {
			if (entity instanceof Zombie zombie && !(entity instanceof BabyMonster) && zombie.isBaby()) {
				ModUtil.babifyMob((Mob) entity);
			}
			if (new Random().nextInt(20) == 0) { ModUtil.babifyMob((Mob) entity); }
		}
		return EventResult.interruptDefault();
	}
}
