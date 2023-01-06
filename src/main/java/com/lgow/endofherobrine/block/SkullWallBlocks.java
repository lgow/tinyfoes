package com.lgow.endofherobrine.block;

import com.lgow.endofherobrine.tileentities.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SkullWallBlocks extends WallSkullBlock {

    public SkullWallBlocks(SkullBlock.Type type, Properties properties) {
        super(type, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return TileEntityInit.SKULL.get().create(pos, state);}
}
