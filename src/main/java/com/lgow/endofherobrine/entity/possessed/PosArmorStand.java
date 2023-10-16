package com.lgow.endofherobrine.entity.possessed;

import com.lgow.endofherobrine.block.BlockInit;
import com.lgow.endofherobrine.entity.PossessedMob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PosArmorStand extends PathfinderMob implements PossessedMob {
	private boolean isOnBase;
	private int coolDown;

	public PosArmorStand(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Override
	protected void registerGoals() {
		registerPosMobGoals(this,false,1.0D);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
		this.moveTo(blockPosition().getCenter());
		pLevel.setBlock(this.blockPosition(), BlockInit.ARMOR_STAND_BASE.get().defaultBlockState(),2);
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}

	@Override
	protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		Item item = itemstack.getItem();
		if (!this.level().isClientSide) {
			if (pPlayer.isCrouching()) {
				if (item instanceof ArmorItem || item instanceof SwordItem) {
					EquipmentSlot slot = getEquipmentSlotForItem(itemstack);
					if (!this.getItemBySlot(slot).isEmpty()) {
						this.spawnAtLocation(getItemBySlot(slot));
					}
					this.setItemSlotAndDropWhenKilled(slot, itemstack.copy());
					if (!pPlayer.getAbilities().instabuild) { pPlayer.getInventory().removeItem(itemstack); }
					this.playSound(slot.getType().equals(EquipmentSlot.Type.ARMOR) ? SoundEvents.ARMOR_EQUIP_GENERIC : SoundEvents.ITEM_FRAME_ADD_ITEM);
					this.coolDown = 20;
					return InteractionResult.SUCCESS;
				}
				else if (itemstack.isEmpty() && coolDown > 0) {
					this.getAllSlots().forEach(stack -> {
						this.spawnAtLocation(stack);
						this.setItemSlot(getEquipmentSlotForItem(stack), ItemStack.EMPTY);
					});
					return InteractionResult.SUCCESS;
				}
			}
		}
		return super.mobInteract(pPlayer, pHand);
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		return SoundEvents.ARMOR_STAND_HIT;
	}



	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ARMOR_STAND_BREAK;
	}

	@Override
	protected void customServerAiStep() {
		if(coolDown>1){
			coolDown--;
		}
//		if(level().getBlockState(blockPosition()).is(BlockInit.ARMOR_STAND_BASE.get()) && !this.isOnBase){
//			isOnBase = true;
//			this.moveTo(getBlockX(),getBlockY() + 0.0625,getBlockZ());
//			this.setDeltaMovement(Vec3.ZERO);
//		}else{
//			isOnBase = false;
//			this.setDeltaMovement(new Vec3(1,1,1));
//		}
		super.customServerAiStep();
	}
}
