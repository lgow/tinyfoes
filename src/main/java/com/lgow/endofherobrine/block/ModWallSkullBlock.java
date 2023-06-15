package com.lgow.endofherobrine.block;

import com.lgow.endofherobrine.tileentities.ModSkullBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModWallSkullBlock extends WallSkullBlock {
	public ModWallSkullBlock(SkullBlock.Type type, Properties properties) {
		super(type, properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	//attaches the block to a block entity
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ModSkullBlockEntity(pos, state);
	}
}
