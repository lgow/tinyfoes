package net.tinyallies.common.entity.projectile;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.tinyallies.common.entity.BabyfiableEntity;
import net.tinyallies.common.entity.ModEntities;
import net.tinyallies.common.registry.ModEffects;

public class BabyfierBlob extends ThrowableProjectile {
	private boolean shouldInvertAge;

	public BabyfierBlob(EntityType<BabyfierBlob> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public BabyfierBlob(LivingEntity pShooter, Level pLevel, boolean shouldInvertAge) {
		super(ModEntities.BLOB.get(), pShooter, pLevel);
		this.shouldInvertAge = shouldInvertAge;
	}

	@Override
	protected void onHit(HitResult pResult) {
		if (!this.level.isClientSide) {
			this.level.broadcastEntityEvent(this, (byte) 3);
			this.discard();
		}
		super.onHit(pResult);
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		if (!level.isClientSide && pResult.getEntity() instanceof LivingEntity livingEntity) {
			if (shouldInvertAge) {
				if (livingEntity.hasEffect(ModEffects.BABYFICATION.get())) {
					livingEntity.removeEffect(ModEffects.BABYFICATION.get());
				}
				if (pResult.getEntity() instanceof Slime slime) {
					slime.setSize(1,true);
				} else if (pResult.getEntity() instanceof Mob mob) {
					mob.setBaby(!mob.isBaby());
				}
				else if (pResult.getEntity() instanceof BabyfiableEntity babyfiableEntity) {
					babyfiableEntity.$setBaby(!babyfiableEntity.$isBaby());
				}
			}
			else {
				livingEntity.addEffect(new MobEffectInstance(ModEffects.BABYFICATION.get(), 260));
			}
		}
		super.onHitEntity(pResult);
	}

	@Override
	protected float getGravity() {
		return 0.0F;
	}

	@Override
	protected void defineSynchedData() {
	}
}
