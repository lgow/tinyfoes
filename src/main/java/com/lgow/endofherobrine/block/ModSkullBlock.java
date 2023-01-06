package com.lgow.endofherobrine.block;

import com.lgow.endofherobrine.tileentities.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModSkullBlock extends SkullBlock {

    public ModSkullBlock(Type type, Properties properties) { super(type, properties);}

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return TileEntityInit.SKULL.get().create(pos, state);}
}
