package net.tinyfoes.common.mixin;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tinyfoes.common.config.TinyFoesConfigs;
import net.tinyfoes.common.entity.BabyfiableEntity;
import net.tinyfoes.common.entity.ai.NearestBabyfiableHostileMobTargetGoal;
import net.tinyfoes.common.registry.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Witch.class)
public abstract class MixinWitch extends Raider implements RangedAttackMob, BabyfiableEntity {
	protected MixinWitch(EntityType<? extends Raider> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract boolean isDrinkingPotion();

	@Inject(method = "registerGoals", at = @At("TAIL"))
	public void registerGoals(CallbackInfo ci) {
		NearestBabyfiableHostileMobTargetGoal<Mob> tinyfoes$babyfyMobsGoal = new NearestBabyfiableHostileMobTargetGoal<>(
				this, Mob.class);
		this.targetSelector.addGoal(2, tinyfoes$babyfyMobsGoal);
	}

	@Override
	public void performRangedAttack(LivingEntity livingEntity, float f) {
		if (!this.isDrinkingPotion()) {
			Vec3 vec3 = livingEntity.getDeltaMovement();
			double d = livingEntity.getX() + vec3.x - this.getX();
			double e = livingEntity.getEyeY() - 1.100000023841858 - this.getY();
			double g = livingEntity.getZ() + vec3.z - this.getZ();
			double h = Math.sqrt(d * d + g * g);
			Holder<Potion> holder = Potions.HARMING;
			if (livingEntity instanceof Raider) {
				if (livingEntity.getHealth() <= 4.0F) {
					holder = Potions.HEALING;
				} else if (!livingEntity.isBaby() && TinyFoesConfigs.WITCH_GOAL.get()) {
					holder = ModEffects.BABYFICATION_POTION;
				}else {
					holder = Potions.REGENERATION;
				}

				this.setTarget((LivingEntity)null);
			} else if (livingEntity instanceof PathfinderMob && TinyFoesConfigs.WITCH_GOAL.get()) {
				holder = ModEffects.BABYFICATION_POTION;
				this.setTarget(null);
			}else if (h >= 8.0 && !livingEntity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
				holder = Potions.SLOWNESS;
			} else if (livingEntity.getHealth() >= 8.0F && !livingEntity.hasEffect(MobEffects.POISON)) {
				holder = Potions.POISON;
			} else if (h <= 3.0 && !livingEntity.hasEffect(MobEffects.WEAKNESS) && this.random.nextFloat() < 0.25F) {
				holder = Potions.WEAKNESS;
			}

			ThrownPotion thrownPotion = new ThrownPotion(this.level(), this);
			thrownPotion.setItem(PotionContents.createItemStack(Items.SPLASH_POTION, holder));
			thrownPotion.setXRot(thrownPotion.getXRot() - -20.0F);
			thrownPotion.shoot(d, e + h * 0.2, g, 0.75F, 8.0F);
			if (!this.isSilent()) {
				this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_THROW, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
			}

			this.level().addFreshEntity(thrownPotion);
		}
	}



	@Override
	public boolean isBaby() {
		return tinyfoes$$isBaby() || tinyfoes$$isBabyfied();
	}

}
