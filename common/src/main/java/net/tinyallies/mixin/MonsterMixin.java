package net.tinyallies.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.tinyallies.entity.ModEntities;
import net.tinyallies.util.ModUtil;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import static net.tinyallies.util.ModUtil.babyficationList;

@Mixin(Monster.class)
public class MonsterMixin extends Mob {
	@Unique private boolean attemptBabyfy = false;

	protected MonsterMixin(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
		if (this.random.nextFloat() < 0.05f) { attemptBabyfy = true; }
		return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
	}

	@Override
	public void tick() {
		if (attemptBabyfy && !this.level.isClientSide) {
			ModUtil.babifyMob(this);
		}
		super.tick();
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
		ItemStack pickResult = this.getPickResult();
		Level level = this.level;
		if (!level.isClientSide && pickResult != null && player.getItemInHand(interactionHand).is(pickResult.getItem())) {
			Mob baby = babyficationList.get(this.getType()).create(level);
			baby.setPos(this.position());
			if (baby.getType().equals(ModEntities.SKELLY.get())) {
				baby.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
			}
			level.addFreshEntity(baby);
			return InteractionResult.sidedSuccess(true);
		}
		return super.mobInteract(player, interactionHand);
	}
}

