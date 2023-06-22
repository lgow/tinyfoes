package net.tinyallies.entity.projectile;

import net.tinyallies.entity.ModEntities;
import net.tinyallies.util.ModUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class BabyfierBlob extends ThrowableProjectile {
	public BabyfierBlob(EntityType<BabyfierBlob> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	protected BabyfierBlob(double pX, double pY, double pZ, Level pLevel) {
		super(ModEntities.BLOB.get(), pX, pY, pZ, pLevel);
	}

	public BabyfierBlob(LivingEntity pShooter, Level pLevel) {
		super(ModEntities.BLOB.get(), pShooter, pLevel);
	}

	@Override
	protected void onHit(HitResult pResult) {
		if (!this.level.isClientSide) {
			this.level.broadcastEntityEvent(this, (byte)3);
			this.discard();
		}
		super.onHit(pResult);
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		if (!level.isClientSide) {
			ModUtil.babifyMob((Mob) pResult.getEntity());
		}
		super.onHitEntity(pResult);
	}

//	private ParticleOptions getParticle() {
//		ItemStack itemstack = this.getItemRaw();
//		return (ParticleOptions)(itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, itemstack));
//	}

//	public void handleEntityEvent(byte pId) {
//		if (pId == 3) {
//			ParticleOptions particleoptions = this.getParticle();
//
//			for(int i = 0; i < 8; ++i) {
//				this.level.addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
//			}
//		}
//
//	}

	@Override
	protected float getGravity() {
		return 0.01F;
	}

	@Override
	protected void defineSynchedData() {
	}
}
