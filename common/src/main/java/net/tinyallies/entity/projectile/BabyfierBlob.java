package net.tinyallies.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.tinyallies.entity.ModEntities;
import net.tinyallies.util.ModUtil;

public class BabyfierBlob extends ThrowableProjectile {
	public BabyfierBlob(EntityType<BabyfierBlob> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public BabyfierBlob(LivingEntity pShooter, Level pLevel) {
		super(ModEntities.BLOB.get(), pShooter, pLevel);
	}

	@Override
	protected void onHit(HitResult pResult) {
		if (!this.level().isClientSide) {
			this.level().broadcastEntityEvent(this, (byte) 3);
			this.discard();
		}
		super.onHit(pResult);
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		if (!level().isClientSide && pResult.getEntity() instanceof Mob mob) {
			ModUtil.babifyMob(mob);
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
