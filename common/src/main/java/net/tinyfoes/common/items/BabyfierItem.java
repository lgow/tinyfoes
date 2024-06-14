package net.tinyfoes.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tinyfoes.common.entity.projectile.BabificationRay;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class BabyfierItem extends ProjectileWeaponItem implements Vanishable {
	private boolean ageInversionMode;

	public BabyfierItem(Item.Properties pProperties) {
		super(pProperties);
	}

	@Override
	public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
		return ItemStack::isEmpty;
	}

	public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
		if (pEntityLiving instanceof Player player) {
			int i = this.getUseDuration(pStack) - pTimeLeft;
			if (i < 5) {
				if (!pLevel.isClientSide()) {
					ageInversionMode = !ageInversionMode;
					player.displayClientMessage(Component.translatable(
									"item.tinyfoes.babyfier." + (ageInversionMode ? "age_inversion" : "babyfication_effect"))
							.withStyle(ChatFormatting.YELLOW), true);
				}
			}
			if (i > 20) {
				if (!pLevel.isClientSide) {
					BabificationRay blob = new BabificationRay(pEntityLiving, pLevel, ageInversionMode);
					blob.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
					pLevel.addFreshEntity(blob);
					pStack.hurtAndBreak(1, player, (player1) -> {
						player1.broadcastBreakEvent(player.getUsedItemHand());
					});
				}
				pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEACON_DEACTIVATE,
						SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	@Override
	public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
		return !player.isCreative();
	}

	public int getUseDuration(ItemStack pStack) {
		return 18000;
	}

	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.BOW;
	}

	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		pPlayer.startUsingItem(pHand);
		return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));
	}

	public int getDefaultProjectileRange() {
		return 30;
	}

	@Override
	public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
		list.add(Component.translatable(
						"item.tinyfoes.babyfier." + (ageInversionMode ? "age_inversion" : "babyfication_effect"))
				.withStyle(ChatFormatting.GRAY));
		list.add(Component.literal(""));
		list.add(Component.translatable("item.tinyfoes.babyfier.tooltip").withStyle(ChatFormatting.YELLOW));
		super.appendHoverText(itemStack, level, list, tooltipFlag);
	}
}
