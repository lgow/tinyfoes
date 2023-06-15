package com.lgow.endofherobrine.block;

import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.herobrine.boss.HerobrineBoss;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Random;

import static net.minecraft.world.level.block.Blocks.*;

public class TotemBlock extends Block {
	private static final BooleanProperty LIT = BlockStateProperties.LIT;

	public final Block baseBlock;

	//Sets the default properties
	public TotemBlock(Block baseBlock) {
		super(Properties.copy(baseBlock)
				.lightLevel((blockState) -> blockState.getValue(BlockStateProperties.LIT) ? 7 : 0));
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
		this.baseBlock = baseBlock;
	}

	//checks if the block is in its respective totem shape
	private boolean isInTotem(BlockPos pos, ServerLevel level) {
		Block totemBase = this.baseBlock.equals(BLACKSTONE) ? DIAMOND_BLOCK : GOLD_BLOCK;
		return level.canSeeSky(pos.above(2)) && level.getBlockState(pos.above()).is(this.baseBlock)
				&& level.getBlockState(pos.below()).is(totemBase) && level.getBlockState(pos.below(2)).is(totemBase);
	}

	//summons a lightning bolt, updates the block property to lit and summons herobrine
	private void activate(ServerLevel level, BlockPos pos, BlockState state) {
		LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
		lightningBolt.setPos(pos.getX(), pos.above(2).getY(), pos.getZ());
		level.addFreshEntity(lightningBolt);
		level.setBlock(pos, state.cycle(LIT), 2);
		this.checkSpawn(level, pos);
	}

	//activates the netherrack totem
	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor accessor, BlockPos pos, BlockPos facingPos) {
		if(accessor instanceof ServerLevel serverLevel && this.canActivateNetherrackTotem(state, serverLevel, pos)){
			this.activate(serverLevel,pos,state);
		}
		return super.updateShape(state, facing, facingState, accessor, pos, facingPos);
	}

	private boolean canActivateNetherrackTotem(BlockState state, ServerLevel serverLevel, BlockPos pos) {
		return this.isInTotem(pos, serverLevel) && !state.getValue(LIT) && !this.baseBlock.equals(BLACKSTONE);
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (pLevel instanceof ServerLevel server && this.baseBlock.equals(BLACKSTONE)) {
			ItemStack itemstack = pPlayer.getItemInHand(pHand);
			if (itemstack.is(Items.NETHER_STAR) && !pState.getValue(LIT)) {
				if (!pPlayer.isCreative()) { itemstack.shrink(1); }
				server.playSound(null, pPlayer.blockPosition(), SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS,
						1.0F, (float) (0.8F + (Math.random() * 0.2D)));
				this.activate(server, pPos, pState);
				return InteractionResult.SUCCESS;
			}
			else {
				return InteractionResult.PASS;
			}
		}
		return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
	}

	private void checkSpawn(ServerLevel server, BlockPos pPos) {
		if (pPos.getY() >= server.getMinBuildHeight()) {
			server.setWeatherParameters(0, 100, false, false);
			HerobrineBoss herobrineBoss = EntityInit.HEROBRINE_BOSS.get().create(server);
			if (this.baseBlock.equals(BLACKSTONE)) {
				herobrineBoss.makeInvulnerable();
				herobrineBoss.setEnraged(true);
			}
			herobrineBoss.moveTo(pPos.getX() + 0.5D, pPos.above(3).getY(), pPos.getZ() + 0.5D, 0.0F, 0.0F);
			for (ServerPlayer serverplayer : server.getEntitiesOfClass(ServerPlayer.class,
					herobrineBoss.getBoundingBox().inflate(50.0D))) {
				CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, herobrineBoss);
			}
			server.addFreshEntity(herobrineBoss);
		}
	}

	//sends a message to all players
	private void broasdcastMessage(ServerLevel server, String component, int bound) {
		server.getServer().getPlayerList().broadcastSystemMessage(Component.literal("<Herobrine> ")
				.append(Component.translatable("totem." + component + new Random().nextInt(bound))), false);
	}

	@Override
	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
		if (pLevel instanceof ServerLevel server){
			if(this.canActivateNetherrackTotem(pState, server, pPos)){
				this.activate(server,pPos,pState);
				return;
			}
			this.broasdcastMessage(server, "placed", 2);
		}
		super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIT);
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		if (level instanceof ServerLevel server && !state.getValue(LIT)) {
			server.setWeatherParameters(0, 6000, true, true);
			this.broasdcastMessage(server, "broken", 3);
			level.playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.BLOCKS, 1.0F,
					(float) (0.8F + (Math.random() * 0.2D)));
		}
		return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
	}
}

