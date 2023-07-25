package net.tinyallies.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.tinyallies.entity.ModEntities;
import net.tinyallies.util.ModUtil;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Creeper.class)
public class CreeperMixin extends Monster {
	protected CreeperMixin(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
		if (this.random.nextFloat() < 0.05f) { ModUtil.babifyMob(this); }
		return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
	}

	@Inject(at = @At("HEAD"), method = "mobInteract", cancellable = true)
	public void mobInteract(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
		ItemStack itemStack = player.getItemInHand(interactionHand);
		if (itemStack.is(this.getPickResult().getItem())) {
			if (!this.level.isClientSide) {
				Mob mob = ModEntities.CREEPY.get().create(level);
				mob.setPos(this.position());
				this.level.addFreshEntity(mob);
			}
			cir.setReturnValue(InteractionResult.sidedSuccess(true));
		}
	}
}

