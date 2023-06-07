package com.lgow.tinyallies.event;

import com.lgow.tinyallies.Main;
import com.lgow.tinyallies.entity.BabyMonster;
import com.lgow.tinyallies.items.ModItems;
import com.lgow.tinyallies.util.ModUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class Events {
	@SubscribeEvent
	public static void Babifyer(LivingHurtEvent event) {
		if (event.getSource().getEntity() instanceof ServerPlayer player && player.getMainHandItem().is(
				ModItems.BABYFIER.get()) && event.getEntity() instanceof Mob mob) {
			if (!mob.isBaby()) {
				ModUtil.babifyMob(mob);
			}
//			else {
//				player.sendSystemMessage(Component.literal("It's already a baby"));
//			}
		}
	}

	@SubscribeEvent
	public static void convertAllBabyZombies(LivingEvent.LivingTickEvent event) {
		LivingEntity living = event.getEntity();
		if (living instanceof Zombie && !(living instanceof BabyMonster) && living.isBaby()) {
			ModUtil.babifyMob((Mob) living);
		}
	}

	@SubscribeEvent
	public void spawnAsBabies(MobSpawnEvent event) {
		Mob living = event.getEntity();
		if (!event.getLevel().isClientSide() && !living.isBaby() && living.tickCount == 1 && new Random().nextInt(20) == 0) {
			ModUtil.babifyMob(living);
		}
	}
}