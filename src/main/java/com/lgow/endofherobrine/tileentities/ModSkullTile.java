package com.lgow.endofherobrine.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModSkullTile extends SkullBlockEntity {

    public ModSkullTile(BlockPos pos, BlockState state) { super(pos, state);}

    @Override
    public BlockEntityType<?> getType() { return TileEntityInit.SKULL.get();}
}