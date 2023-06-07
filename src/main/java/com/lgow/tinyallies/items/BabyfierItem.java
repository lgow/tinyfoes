package com.lgow.tinyallies.items;

import com.lgow.tinyallies.entity.projectile.BabyfierBlob;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class BabyfierItem extends ProjectileWeaponItem implements Vanishable {
	public BabyfierItem(Item.Properties pProperties) {
		super(pProperties);
	}

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return null;
	}

	public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
		if (pEntityLiving instanceof Player player) {
			int i = this.getUseDuration(pStack) - pTimeLeft;
			if (i < 20) { return; }
			if (!pLevel.isClientSide) {
				BabyfierBlob blob = new BabyfierBlob(pEntityLiving, pLevel);
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

	public int getUseDuration(ItemStack pStack) {
		return 72000;
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
}
