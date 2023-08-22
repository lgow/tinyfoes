package com.lgow.endofherobrine.block;

import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.herobrine.boss.HerobrineBoss;
import com.lgow.endofherobrine.world.data.ModSavedData;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Random;

import static net.minecraft.world.level.block.Blocks.*;

public class TotemBlock extends Block {
	private static final EnumProperty<TotemStates> STATE = EnumProperty.create("state", TotemStates.class);
	public final Block baseBlock;
	private final boolean isBlackstone;

	//Sets the default properties
	public TotemBlock(Block baseBlock) {
		super(Properties.copy(baseBlock).lightLevel((blockState) -> blockState.getValue(STATE)
				.equals(TotemStates.INACTIVE) ? 0 : 7).pushReaction(PushReaction.BLOCK));
		this.registerDefaultState(this.stateDefinition.any().setValue(STATE, TotemStates.INACTIVE));
		this.baseBlock = baseBlock;
		this.isBlackstone = this.baseBlock.equals(BLACKSTONE);
	}

	//checks if the block is in its respective totem shape
	private boolean isInTotem(BlockPos pos, ServerLevel level) {
		Block totemBase = isBlackstone ? DIAMOND_BLOCK : GOLD_BLOCK;
		return level.canSeeSky(pos.above(2)) && level.getBlockState(pos.above()).is(this.baseBlock)
				&& level.getBlockState(pos.below()).is(totemBase) && level.getBlockState(pos.below(2)).is(totemBase);
	}

	//checks if the block is in its respective totem shape
	private boolean isInAltar(BlockPos pos, ServerLevel level) {
		BlockPos below = pos.below();
		return !isBlackstone && level.canSeeSky(pos.above()) && level.getBlockState(below).is(SOUL_SOIL)
				&& level.getBlockState(pos.above()).is(FIRE) //
				&& level.getBlockState(pos.east().north()).is(REDSTONE_TORCH) //
				&& level.getBlockState(pos.north().west()).is(REDSTONE_TORCH) //
				&& level.getBlockState(pos.west().south()).is(REDSTONE_TORCH) //
				&& level.getBlockState(pos.south().east()).is(REDSTONE_TORCH) && level.getBlockState(below.east()).is(
				GOLD_BLOCK) //
				&& level.getBlockState(below.east().north()).is(GOLD_BLOCK) //
				&& level.getBlockState(below.north()).is(GOLD_BLOCK) //
				&& level.getBlockState(below.north().west()).is(GOLD_BLOCK) //
				&& level.getBlockState(below.west()).is(GOLD_BLOCK) //
				&& level.getBlockState(below.west().south()).is(GOLD_BLOCK) //
				&& level.getBlockState(below.south()).is(GOLD_BLOCK) //
				&& level.getBlockState(below.south().east()).is(GOLD_BLOCK);
	}

	//summons a lightning bolt, updates the block property to lit and summons herobrine
	private void activate(ServerLevel level, BlockPos pos, BlockState state) {
		LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
		lightningBolt.setPos(pos.getX(), pos.above(2).getY(), pos.getZ());
		level.addFreshEntity(lightningBolt);
		level.setBlock(pos, state.setValue(STATE, TotemStates.ACTIVE), 2);
		this.checkSpawn(level, pos);
	}

	//summons a lightning bolt, updates the block property to lit and summons herobrine
	private void overcharge(ServerLevel level, BlockPos pos, BlockState state) {
		level.setBlock(pos, state.setValue(STATE, TotemStates.OVERCHARGED), 2);
		ModSavedData.get(level.getServer()).setResurrectedHerobrine(true);
	}

	//netherrack totem activation conditions
	private boolean canActivateNetherrackTotem(BlockState state, ServerLevel serverLevel, BlockPos pos) {
		return !isBlackstone && state.getValue(STATE).equals(TotemStates.INACTIVE) && this.isInTotem(pos, serverLevel)
				&& !ModSavedData.get(serverLevel.getServer()).hasDefeatedHerobrine();
	}

	//netherrack totem activation conditions
	private boolean canActivateBlackstoneTotem(BlockState state, ServerLevel serverLevel, BlockPos pos) {
		ModSavedData levelData = ModSavedData.get(serverLevel.getServer());
		return state.getValue(STATE).equals(TotemStates.INACTIVE) && this.isInTotem(pos, serverLevel)
				&& levelData.hasDefeatedHerobrine() && !levelData.isHerobrineDeadOrResting();
	}

	private void checkSpawn(ServerLevel server, BlockPos pPos) {
		if (pPos.getY() >= server.getMinBuildHeight()) {
			HerobrineBoss herobrineBoss = EntityInit.HEROBRINE_BOSS.get().create(server);
			if (isBlackstone) {
				//				herobrineBoss.makeInvulnerable();
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
		if (!isBlackstone && !ModSavedData.get(server.getServer()).isHerobrineDeadOrResting()) {
			server.getServer().getPlayerList().broadcastSystemMessage(Component.literal("<Herobrine> ")
					.append(Component.translatable("totem." + component + new Random().nextInt(bound))), false);
		}
	}

	@Override
	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
		if (pLevel instanceof ServerLevel server) {
			if (this.canActivateNetherrackTotem(pState, server, pPos)) {
				this.activate(server, pPos, pState);
			}
			else if (isInAltar(pPos, server)) {
				this.overcharge(server, pPos, pState);
			}
			this.broasdcastMessage(server, "placed", 2);
		}
		super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		if (level instanceof ServerLevel server && state.getValue(STATE).equals(TotemStates.INACTIVE)) {
			if (!isBlackstone) {
				if (server.isRaining()) {
					server.setWeatherParameters(0, 6000, true, true);
				}
				level.playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.BLOCKS,
						1.0F, (float) (0.8F + (Math.random() * 0.2D)));
				this.broasdcastMessage(server, "broken", 3);
			}
		}
		return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (pLevel instanceof ServerLevel server && isBlackstone) {
			ItemStack itemstack = pPlayer.getItemInHand(pHand);
			if (itemstack.is(Items.NETHER_STAR) && canActivateBlackstoneTotem(pState, server, pPos)) {
				if (!pPlayer.isCreative()) { itemstack.shrink(1); }
				server.playSound(null, pPlayer.blockPosition(), SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS,
						1.0F, (float) (0.8F + (Math.random() * 0.2D)));
				this.activate(server, pPos, pState);
				return InteractionResult.SUCCESS;
			}
		}
		return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor accessor, BlockPos pos, BlockPos facingPos) {
		if (accessor instanceof ServerLevel serverLevel) {
			if (this.canActivateNetherrackTotem(state, serverLevel, pos)) {
				this.activate(serverLevel, pos, state);
			}
			else if (isInAltar(pos, serverLevel)) {
				this.overcharge(serverLevel, pos, state);
			}
		}
		return super.updateShape(state, facing, facingState, accessor, pos, facingPos);
	}

	@Override
	public void updateIndirectNeighbourShapes(BlockState pState, LevelAccessor pLevel, BlockPos pPos, int pFlags, int pRecursionLeft) {
		if (pLevel instanceof ServerLevel serverLevel) {
			if (isInAltar(pPos, serverLevel)) {
				this.overcharge(serverLevel, pPos, pState);
			}
		}
		super.updateIndirectNeighbourShapes(pState, pLevel, pPos, pFlags, pRecursionLeft);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(STATE);
	}
//
//	@Override
//	public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
//		if (!pState.getValue(STATE).equals(TotemStates.INACTIVE) && !this.isInTotem(pPos, pLevel) && !this.isInAltar(
//				pPos, pLevel)) {
//			pState.setValue(STATE,TotemStates.INACTIVE);
//		}
//		super.tick(pState, pLevel, pPos, pRandom);
//	}
}

