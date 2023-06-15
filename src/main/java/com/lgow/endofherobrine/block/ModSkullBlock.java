package com.lgow.endofherobrine.block;

import com.lgow.endofherobrine.tileentities.ModSkullBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModSkullBlock extends SkullBlock {
	public ModSkullBlock(Type type, Properties properties) { super(type, properties); }

	//attaches the block to a block entity
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ModSkullBlockEntity(pos, state);
	}

	public enum Types implements Type {
		HEROBRINE, CURSED
	}
}
