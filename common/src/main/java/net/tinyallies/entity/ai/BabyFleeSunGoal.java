package net.tinyallies.entity.ai;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tinyallies.entity.BabyMonster;

public class BabyFleeSunGoal extends FleeSunGoal {
	private final Level level = this.mob.level;
	private final BabyMonster baby = (BabyMonster) this.mob;
	private final LivingEntity owner1 = baby.getOwner();

	public BabyFleeSunGoal(PathfinderMob pathfinderMob, double d) {
		super(pathfinderMob, d);
	}

	@Override
	public boolean canUse() {
		if (!this.level.canSeeSky(this.mob.blockPosition())) {
			return false;
		}
		if (!this.level.isDay()) {
			return false;
		}
		if(owner1 !=null){
			ItemStack helmet = this.mob.getItemBySlot(EquipmentSlot.HEAD);
			if (helmet.isEmpty() || (helmet.getMaxDamage() - helmet.getDamageValue()) >= helmet.getMaxDamage() / 3) {
				return false;
			}
			LivingEntity lastHurtByMob = owner1.getLastHurtByMob();
			if (lastHurtByMob != null && lastHurtByMob.getLastHurtMob() == owner1 && lastHurtByMob.getCombatTracker().getCombatDuration() > 0) {
				return false;
			}
			LivingEntity lastHurtMob = owner1.getLastHurtMob();
			if (lastHurtMob != null && (lastHurtMob.isAlive() || lastHurtMob.distanceToSqr(owner1) > 14.0D)) {
				return false;
			}
		}
		return this.setWantedPos();
	}
}
