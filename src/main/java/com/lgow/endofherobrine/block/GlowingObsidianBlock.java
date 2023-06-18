package com.lgow.endofherobrine.block;

import com.lgow.endofherobrine.item.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Collections;
import java.util.List;

public class GlowingObsidianBlock extends ModInfestedBlock {
	private static final BooleanProperty INFESTED = BooleanProperty.create("infested");

	//Sets the block properties and the default blockstate property
	public GlowingObsidianBlock() {
		super(Blocks.OBSIDIAN, Properties.copy(Blocks.STONE).mapColor(MapColor.COLOR_RED)
				.requiresCorrectToolForDrops().strength(10F, 450F).lightLevel((blockState) -> 12));
		this.registerDefaultState(this.stateDefinition.any().setValue(INFESTED, false));
	}

	//Registers the blockstate property
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(INFESTED);
	}

	//Infests the block when using a possessed silverfish spawn egg on it
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (level instanceof ServerLevel server) {
			ItemStack itemstack = player.getItemInHand(handIn);
			if (itemstack.is(ItemInit.SILVERFISH_SPAWN_EGG.get()) && !this.isInfested(state)) {
				if (!player.isCreative()) { itemstack.shrink(1); }
				level.setBlock(pos, state.cycle(INFESTED), 2);
				server.playSound(null, player.blockPosition(), SoundEvents.SILVERFISH_AMBIENT, SoundSource.BLOCKS,
						1.0F, (float) (0.8F + (Math.random() * 0.2D)));
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		}
		return super.use(state, level, pos, player, handIn, hit);
	}

	//If infested, spawns silverfish when broken
	public void spawnAfterBreak(BlockState pState, ServerLevel server, BlockPos pos, ItemStack stack, boolean b) {
		if(this.isInfested(pState)) {
			this.spawnInfestation(server,pos);
		}
	}

	//If not infested, drops itself when broken
	@Override
	public List<ItemStack> getDrops(BlockState pState, LootParams.Builder p_287596_) {
		if(this.isInfested(pState)){
			return Collections.emptyList();
		}
		return super.getDrops(pState, p_287596_);
	}

	//Checks if the block is infested
	public boolean isInfested(BlockState state) {
		return state.getValue(INFESTED);
	}
}
