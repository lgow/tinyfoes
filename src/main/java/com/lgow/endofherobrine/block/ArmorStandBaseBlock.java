package com.lgow.endofherobrine.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ArmorStandBaseBlock extends FallingBlock implements SimpleWaterloggedBlock {
	protected static final VoxelShape CEILING_AABB_Z = Block.box(2.0D, 0D, 2.0D, 14.0D, 1.0D, 14.0D);
	public ArmorStandBaseBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return CEILING_AABB_Z;
	}
}
