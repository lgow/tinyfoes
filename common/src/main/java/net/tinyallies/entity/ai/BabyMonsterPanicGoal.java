package net.tinyallies.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.level.block.state.BlockState;
import net.tinyallies.entity.BabyMonster;

public class BabyMonsterPanicGoal extends PanicGoal {
	private BabyMonster baby;

	public BabyMonsterPanicGoal(PathfinderMob mob, double pSpeedModifier) {
		super(mob, pSpeedModifier);
		if (this.mob instanceof BabyMonster baby) {
			this.baby = baby;
		}
	}

	protected boolean shouldPanic() {
		if (this.mob.isOnFire()) {
			BlockPos blockPos = this.mob.blockPosition();
			BlockState blockState = this.mob.level.getBlockState(blockPos);
			return !this.baby.isUndead() || !this.mob.level.canSeeSky(blockPos) && !blockState.is(BlockTags.FIRE)
					&& !blockState.getFluidState().is(FluidTags.LAVA);
		}
		return this.mob.isFreezing();
	}

	@Override
	public boolean canContinueToUse() {
		return this.shouldPanic() || super.canContinueToUse();
	}
}