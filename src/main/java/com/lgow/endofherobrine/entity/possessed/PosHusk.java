package com.lgow.endofherobrine.entity.possessed;

import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.PossessedMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PosHusk extends Husk implements PossessedMob {
	public PosHusk(EntityType<? extends PosHusk> type, Level level) { super(type, level); }

	public static AttributeSupplier.Builder setCustomAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 35).add(Attributes.MOVEMENT_SPEED, 0.23F)
				.add(Attributes.FOLLOW_RANGE, 40).add(Attributes.ATTACK_DAMAGE, 6.0D).add(Attributes.ARMOR, 4.0D).add(
						Attributes.SPAWN_REINFORCEMENTS_CHANCE);
	}

	@Override
	protected SoundEvent getAmbientSound() { return null; }

	@Override
	public boolean doHurtTarget(Entity entityIn) {
		boolean flag = super.doHurtTarget(entityIn);
		if (flag && this.getMainHandItem().isEmpty() && entityIn instanceof LivingEntity) {
			float f = this.level().getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
			((LivingEntity) entityIn).addEffect(new MobEffectInstance(MobEffects.HUNGER, 280 * (int) f));
		}
		return flag;
	}

	@Override
	protected ResourceLocation getDefaultLootTable() { return new ResourceLocation("entities/husk"); }

	@Override
	protected void registerGoals() {
		this.registerPosMonsterGoals(this, true);
		this.goalSelector.addGoal(0, new ZombieAttackGoal(this, 1, true));
		this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	public MobType getMobType() { return ModMobTypes.POSSESSED; }
}
