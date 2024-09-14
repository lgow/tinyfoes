package net.tinyfoes.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tinyfoes.common.entity.projectile.BabificationRay;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class BabyfierItem extends ProjectileWeaponItem {
	private boolean ageInversionMode;

	public BabyfierItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
	}

	public static float getPowerForTime(int i) {
		float f = (float) i / 20.0F;
		f = (f * f + f * 2.0F) / 3.0F;
		if (f > 1.0F) {
			f = 1.0F;
		}
		return f;
	}

	@Override
	public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
		return ItemStack::isEmpty;
	}

	public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i) {
		if (livingEntity instanceof Player player) {
			ItemStack itemStack2 = player.getProjectile(itemStack);
			if (!itemStack2.isEmpty()) {
				int j = this.getUseDuration(itemStack, livingEntity) - i;
				if (j < 5) {
					if (!level.isClientSide()) {
						ageInversionMode = !ageInversionMode;
						player.displayClientMessage(Component.translatable(
										"item.tinyfoes.babyfier.mode." + (ageInversionMode ? "age_inversion" : "temporary"))
								.withStyle(ChatFormatting.YELLOW), true);
					}
				}
				else {
					if (level instanceof ServerLevel) {
						BabificationRay blob = new BabificationRay(livingEntity, level, ageInversionMode);
						blob.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
						level.addFreshEntity(blob);
					}
					level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEACON_DEACTIVATE,
							SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
					player.awardStat(Stats.ITEM_USED.get(this));
				}
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
	protected void shootProjectile(LivingEntity livingEntity, Projectile projectile, int i, float f, float g, float h, @Nullable LivingEntity livingEntity2) {
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
		list.add(Component.translatable(
						"item.tinyfoes.babyfier.mode." + (ageInversionMode ? "age_inversion" : "temporary"))
				.withStyle(ChatFormatting.BLUE));
		list.add(Component.translatable(
						"item.tinyfoes.babyfier.tooltip." + (ageInversionMode ? "age_inversion" : "temporary"))
				.withStyle(ChatFormatting.GRAY));
		list.add(Component.literal(""));
		list.add(Component.translatable("item.tinyfoes.babyfier.tooltip").withStyle(ChatFormatting.YELLOW));
		super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
	}
}
