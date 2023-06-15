package com.lgow.endofherobrine.entity.possessed;

import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.PossessedMob;
import com.lgow.endofherobrine.entity.ai.StarePlayerGoal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PosStray extends Stray {
	public PosStray(EntityType<? extends PosStray> type, Level level) { super(type, level); }

	public static AttributeSupplier.Builder setCustomAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 35).add(Attributes.FOLLOW_RANGE, 40).add(
				Attributes.MOVEMENT_SPEED, 0.23F).add(Attributes.ATTACK_DAMAGE, 6.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() { return null; }

	@Override
	protected AbstractArrow getArrow(ItemStack pArrowStack, float pDistanceFactor) {
		AbstractArrow abstractarrow = super.getArrow(pArrowStack, pDistanceFactor);
		if (abstractarrow instanceof Arrow) {
			((Arrow) abstractarrow).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200));
		}
		return abstractarrow;
	}


	@Override
	protected ResourceLocation getDefaultLootTable() { return new ResourceLocation("minecraft", "entities/stray"); }

	@Override
	protected boolean isSunBurnTick() {
		return false;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new RangedBowAttackGoal<>(this, 1.0D, 15, 30.0F));
		this.goalSelector.addGoal(2, new StarePlayerGoal(this, 40));
		this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
	}

	@Override
	public MobType getMobType() { return ModMobTypes.POSSESSED; }
}
