package com.lgow.endofherobrine.block;

import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.possessed.PosSilverfish;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ModInfestedBlock extends Block {

	//Sets the default block properties
	public ModInfestedBlock(Block pHostBlock, Properties pProperties) {
		super(pProperties.destroyTime(pHostBlock.defaultDestroyTime() / 2.0F).explosionResistance(0.75F));
	}

	protected void spawnInfestation(ServerLevel world, BlockPos pos) {
		PosSilverfish posSilverfish = EntityInit.P_SILVERFISH.get().create(world);
		posSilverfish.moveTo((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, 0.0F, 0.0F);
		world.addFreshEntity(posSilverfish);
		posSilverfish.spawnAnim();
	}

	//Spawns silverfish when broken
	public void spawnAfterBreak(BlockState state, ServerLevel server, BlockPos pos, ItemStack stack, boolean b) {
		this.spawnInfestation(server, pos);
	}
}
