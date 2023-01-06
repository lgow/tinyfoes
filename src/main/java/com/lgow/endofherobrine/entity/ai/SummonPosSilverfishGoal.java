package com.lgow.endofherobrine.entity.ai;

import com.lgow.endofherobrine.entity.possessed.PosSilverfish;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SummonPosSilverfishGoal extends Goal {

    private final PosSilverfish silverfish;
    private int lookForFriends;

    public SummonPosSilverfishGoal(PosSilverfish silverfish) {
        this.silverfish = silverfish;
    }

    public void notifyHurt() {
        if (this.lookForFriends == 0) {
            this.lookForFriends = this.adjustedTickDelay(20);
        }
    }

    public boolean canUse() {
        return this.lookForFriends > 0;
    }

    public void tick() {
        --this.lookForFriends;
        if (this.lookForFriends <= 0) {
            Level level = this.silverfish.level;
            RandomSource randomsource = this.silverfish.getRandom();
            BlockPos blockpos = this.silverfish.blockPosition();

            for (int i = 0; i <= 5 && i >= -5; i = (i <= 0 ? 1 : 0) - i) {
                for (int j = 0; j <= 10 && j >= -10; j = (j <= 0 ? 1 : 0) - j) {
                    for (int k = 0; k <= 10 && k >= -10; k = (k <= 0 ? 1 : 0) - k) {
                        BlockPos blockpos1 = blockpos.offset(j, i, k);
                        BlockState blockstate = level.getBlockState(blockpos1);
                        Block block = blockstate.getBlock();
                        if (block instanceof InfestedBlock) {
                            if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(level, this.silverfish)) {
                                level.destroyBlock(blockpos1, true, this.silverfish);
                            } else {
                                level.setBlock(blockpos1, ((InfestedBlock) block).hostStateByInfested(level.getBlockState(blockpos1)), 3);
                            }
                            if (randomsource.nextBoolean()) {
                                return;
                            }
                        }
                    }
                }
            }
        }

    }
}
