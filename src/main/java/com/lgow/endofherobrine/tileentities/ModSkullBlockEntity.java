package com.lgow.endofherobrine.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModSkullBlockEntity extends SkullBlockEntity {
	public ModSkullBlockEntity(BlockPos pos, BlockState state) { super(pos, state); }

	@Override
	public BlockEntityType<?> getType() { return BlockEntityInit.SKULL.get(); }
}