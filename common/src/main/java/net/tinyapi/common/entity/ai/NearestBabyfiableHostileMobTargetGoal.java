package net.tinyapi.common.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

public class NearestBabyfiableHostileMobTargetGoal <T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
	private static final int DEFAULT_COOLDOWN = 200;
	private int cooldown = 0;

	public NearestBabyfiableHostileMobTargetGoal(Mob mob, Class<T> classT) {
		super(mob, classT, 500, true, false,
				(livingEntity -> livingEntity instanceof Mob mob1 && !livingEntity.isBaby() && !livingEntity.getType()
						.getCategory().isFriendly() && mob1.getTarget() instanceof Player && mob1.distanceTo(
						mob1.getTarget()) < mob.distanceTo(mob1.getTarget())));
	}

	public int getCooldown() {
		return this.cooldown;
	}

	public void decrementCooldown() {
		--this.cooldown;
	}

	public boolean canUse() {
		if (getCooldown() <= 0 && this.mob.getRandom().nextBoolean()) {
			this.findTarget();
			return this.target != null;
		}
		else {
			decrementCooldown();
			return false;
		}
	}

	public void start() {
		this.cooldown = reducedTickDelay(200);
		super.start();
	}
}