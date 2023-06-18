package com.lgow.endofherobrine.entity.possessed;

import com.lgow.endofherobrine.block.GlowingObsidianBlock;
import com.lgow.endofherobrine.block.ModInfestedBlock;
import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.PossessedMob;
import com.lgow.endofherobrine.entity.Teleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class PosSilverfish extends Silverfish implements Teleporter {
	private SummonPosSilverfishGoal summonPosSilverfish;

	public PosSilverfish(EntityType<? extends PosSilverfish> type, Level level) { super(type, level); }

	public static AttributeSupplier.Builder setCustomAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8).add(Attributes.MOVEMENT_SPEED, 0.25D)
				.add(Attributes.FOLLOW_RANGE, 40D).add(Attributes.ATTACK_DAMAGE, 1D);
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return new ResourceLocation("minecraft", "entities/silverfish");
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		boolean hasTpPos;
		if (target instanceof Player player && player.getHealth() > 1) {
			do {
				hasTpPos = this.checkTeleportPos(player);
			}
			while (!hasTpPos);
		}
		return super.doHurtTarget(target);
	}

	@Override
	protected void registerGoals() {
		this.summonPosSilverfish = new SummonPosSilverfishGoal(this);
		this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(3, this.summonPosSilverfish);
		this.goalSelector.addGoal(6, new FloatGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, (new HurtByTargetGoal(this)).setAlertOthers());
	}

	@Override
	protected SoundEvent getAmbientSound() { return null; }

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (summonPosSilverfish != null) { this.summonPosSilverfish.notifyHurt(); }
		return super.hurt(source, amount);
	}

	@Override
	public MobType getMobType() { return ModMobTypes.POSSESSED; }

	protected boolean checkTeleportPos(Player player) {
		if (!player.level().isClientSide && player.isAlive()) {
			double d0 = player.getX() + (this.random.nextInt(800) - 400.5);
			double d1 = player.getY() + (this.random.nextInt(64) + 12);
			double d2 = player.getZ() + (this.random.nextInt(800) - 400.5);
			return this.attemptTeleport(player, d0, d1, d2);
		}
		return false;
	}

	@Override
	public void onAddedToWorld() {
		List<PosSilverfish> list = this.level().getEntitiesOfClass(PosSilverfish.class,
				this.getBoundingBox().inflate(30));
		if (summonPosSilverfish != null && list.size() == 1) { this.summonPosSilverfish.notifyHurt(); }
		super.onAddedToWorld();
	}

	@Override
	protected void dropAllDeathLoot(DamageSource source) {
		if (summonPosSilverfish != null) { this.summonPosSilverfish.notifyHurt(); }
		super.dropAllDeathLoot(source);
	}

	public static class SummonPosSilverfishGoal extends Goal {
		private final PosSilverfish silverfish;

		private int lookForFriends;

		public SummonPosSilverfishGoal(PosSilverfish silverfish) {
			this.silverfish = silverfish;
		}

		public void notifyHurt() {
			if (this.lookForFriends == 0) {
				this.lookForFriends = this.adjustedTickDelay(20);
			}
		}

		@Override
		public boolean canUse() {
			return this.lookForFriends > 0;
		}

		@Override
		public void tick() {
			--this.lookForFriends;
			if (this.lookForFriends <= 0) {
				Level level = this.silverfish.level();
				RandomSource randomsource = this.silverfish.getRandom();
				BlockPos blockpos = this.silverfish.blockPosition();
				for (int i = 0; i <= 5 && i >= -5; i = (i <= 0 ? 1 : 0) - i) {
					for (int j = 0; j <= 10 && j >= -10; j = (j <= 0 ? 1 : 0) - j) {
						for (int k = 0; k <= 10 && k >= -10; k = (k <= 0 ? 1 : 0) - k) {
							BlockPos blockpos1 = blockpos.offset(j, i, k);
							BlockState blockstate = level.getBlockState(blockpos1);
							Block block = blockstate.getBlock();
							if (block instanceof GlowingObsidianBlock gobs && !gobs.isInfested(blockstate)) {
								return;
							}
							if (block instanceof ModInfestedBlock) {
								if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(level,
										this.silverfish)) {
									level.destroyBlock(blockpos1, true, this.silverfish);
								}
								if (randomsource.nextBoolean()) {
									return;
								}
							}
						}
					}
				}
			}
		}
	}
}



