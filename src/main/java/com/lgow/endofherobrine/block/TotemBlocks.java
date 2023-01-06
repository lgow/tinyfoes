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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Random;

import static net.minecraft.world.level.block.Blocks.*;

public class TotemBlocks extends Block {

    public final Block baseBlock;
    private boolean isLit;
    private final boolean isBlackstone;

    private static final BooleanProperty LIT = BlockStateProperties.LIT;

    public TotemBlocks(Block block) {
        super(Properties.copy(block).lightLevel((blockState) -> blockState.getValue(BlockStateProperties.LIT) ? 7 : 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
        this.baseBlock = block;
        this.isBlackstone = block.equals(BLACKSTONE);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    public boolean isTotem(BlockPos pos, Level level){
        Block totemBase = this.isBlackstone ? DIAMOND_BLOCK : GOLD_BLOCK;
        return  level.canSeeSky(pos.above(2))
                && level.getBlockState(pos.above()).is(this.baseBlock)
                && level.getBlockState(pos.below()).is(totemBase)
                && level.getBlockState(pos.below(2)).is(totemBase);
    }

    public void activateTotem(Level level, BlockPos pos, BlockState state){
        LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
        lightningBolt.setPos(pos.getX(), pos.above(2).getY() , pos.getZ());
        level.addFreshEntity(lightningBolt);
        level.setBlock(pos, state.cycle(LIT), 2);
        this.checkSpawn(level, pos);
    }

    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor accessor, BlockPos pos, BlockPos facingPos) {
        Level level = (Level) accessor;
        this.isLit = state.getValue(LIT);
        if(this.isTotem(pos,level) && !isLit && !isBlackstone){
                this.activateTotem(level, pos, state);
        }
        return super.updateShape(state, facing, facingState, accessor, pos, facingPos);
    }

    public void checkSpawn(Level pLevel, BlockPos pPos) {
        if (pLevel instanceof ServerLevel server) {
            if (pPos.getY() >= pLevel.getMinBuildHeight()) {
                server.setWeatherParameters(0,100,false,false);
                HerobrineBoss herobrineBoss = EntityInit.HEROBRINE_BOSS.get().create(pLevel);
                if(this.isBlackstone) {
                    herobrineBoss.makeInvulnerable();
                    herobrineBoss.setEnraged(true);
                }
                herobrineBoss.moveTo(pPos.getX() + 0.5D, pPos.above(3).getY(), pPos.getZ() + 0.5D, 0.0F, 0.0F);
                for(ServerPlayer serverplayer : pLevel.getEntitiesOfClass(ServerPlayer.class, herobrineBoss.getBoundingBox().inflate(50.0D))) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, herobrineBoss);
                }
                pLevel.addFreshEntity(herobrineBoss);
                }
            }
        }


    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack itemstack = player.getItemInHand(handIn);
        if (itemstack.is(Items.NETHER_STAR) && !isLit && isBlackstone) {
            if (!player.isCreative()) {itemstack.shrink(1);}
            level.playSound(null, player.blockPosition(), SoundEvents.END_PORTAL_FRAME_FILL,
                    SoundSource.MASTER, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
            this.activateTotem(level, pos, state);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    public void broasdcastMessage(Level level, String component, int bound){
        level.getServer().getPlayerList().broadcastSystemMessage(Component.literal("<Herobrine> ")
                .append(Component.translatable("totem." + component + new Random().nextInt(bound))), false);
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if(!level.isClientSide){
            this.broasdcastMessage(level,"placed", 2);
        }
    }

    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean shouldDrop, FluidState fluid) {
        if (level instanceof ServerLevel server) {
            server.setWeatherParameters(0, 6000, true, true);
            this.broasdcastMessage(level, "broken", 3);
            level.playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.MASTER,
                    1.0F, (float) (0.8F + (Math.random() * 0.2D)));
        }
        return super.onDestroyedByPlayer(state, level, pos, player, shouldDrop, fluid);
    }
}

