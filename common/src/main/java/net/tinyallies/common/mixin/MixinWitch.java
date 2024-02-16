package net.tinyallies.common.mixin;

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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tinyallies.common.entity.ai.NearestBabyfiableHostileMobTargetGoal;
import net.tinyallies.common.registry.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;

@Mixin(Witch.class)
public abstract class MixinWitch extends Raider implements RangedAttackMob {
	@Unique private NearestBabyfiableHostileMobTargetGoal babyfyMobsGoal;

	protected MixinWitch(EntityType<? extends Raider> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract boolean isDrinkingPotion();

	@Inject(method = "registerGoals", at = @At("TAIL"))
	public void registerGoals(CallbackInfo ci) {
		this.babyfyMobsGoal = new NearestBabyfiableHostileMobTargetGoal(this, Mob.class);
		this.targetSelector.addGoal(2, this.babyfyMobsGoal);
	}

	@Override
	public void performRangedAttack(LivingEntity livingEntity, float f) {
		if (!this.isDrinkingPotion()) {
			Vec3 vec3 = livingEntity.getDeltaMovement();
			double d = livingEntity.getX() + vec3.x - this.getX();
			double e = livingEntity.getEyeY() - 1.100000023841858 - this.getY();
			double g = livingEntity.getZ() + vec3.z - this.getZ();
			double h = Math.sqrt(d * d + g * g);
			Potion potion = Potions.HARMING;
			if (livingEntity instanceof Raider) {
				if (livingEntity.getHealth() <= 4.0F) {
					potion = Potions.HEALING;
				}
				else if (!livingEntity.isBaby()) {
					potion = ModEffects.BABYFICATION_POTION.get();
				}
				else {
					potion = Potions.REGENERATION;
				}
				this.setTarget(null);
			}
			else if (livingEntity instanceof PathfinderMob) {
				potion = ModEffects.BABYFICATION_POTION.get();
				this.setTarget(null);
			}
			else if (h >= 8.0 && !livingEntity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
				potion = Potions.SLOWNESS;
			}
			else if (livingEntity.getHealth() >= 8.0F && !livingEntity.hasEffect(MobEffects.POISON)) {
				potion = Potions.POISON;
			}
			else if (h <= 3.0 && !livingEntity.hasEffect(MobEffects.WEAKNESS) && this.random.nextFloat() < 0.25F) {
				potion = Potions.WEAKNESS;
			}
			ThrownPotion thrownPotion = new ThrownPotion(this.level, this);
			thrownPotion.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), potion));
			thrownPotion.setXRot(thrownPotion.getXRot() - -20.0F);
			thrownPotion.shoot(d, e + h * 0.2, g, 0.75F, 8.0F);
			if (!this.isSilent()) {
				this.level.playSound((Player) null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_THROW,
						this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
			}
			this.level.addFreshEntity(thrownPotion);
		}
	}
}
