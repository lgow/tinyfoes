package net.tinyallies.entity.projectile;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class BabyfiableMonster extends Monster{
	protected BabyfiableMonster(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public void thunderHit(ServerLevel serverLevel, LightningBolt lightningBolt) {
		lightningBolt.sendSystemMessage(Component.literal("hello"));
		super.thunderHit(serverLevel, lightningBolt);
	}
}
