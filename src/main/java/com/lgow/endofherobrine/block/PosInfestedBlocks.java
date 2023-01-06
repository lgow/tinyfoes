package com.lgow.endofherobrine.block;

import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.possessed.PosSilverfish;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PosInfestedBlocks extends InfestedBlock {

    public PosInfestedBlocks(Block blockIn, Properties properties) { super(blockIn, properties);}

    public void spawnInfestation(ServerLevel world, BlockPos pos) {
        PosSilverfish silverfishentity = EntityInit.POS_SILVERFISH.get().create(world);
        silverfishentity.moveTo((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, 0.0F, 0.0F);
        world.addFreshEntity(silverfishentity);
        silverfishentity.spawnAnim();
    }
    //should spawn reinforcements?
    public void spawnAfterBreak(BlockState state, ServerLevel server, BlockPos pos, ItemStack stack, boolean b) {
            this.spawnInfestation(server, pos);
    }
}
