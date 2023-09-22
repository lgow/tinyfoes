package com.lgow.endofherobrine.entity.herobrine;



//todo teleport to nearest player, hold best weapon and equip best armor

import com.lgow.endofherobrine.entity.Teleporter;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
//
//public class Doppelganger extends AbstractHerobrine implements Teleporter {
//////todo player quits game doppleganger disappears
//    public float oBob, bob;
//    public double xCloakO, yCloakO, zCloakO, xCloak, yCloak, zCloak;
//
//    public Doppelganger(EntityType<? extends Monster> type, Level level) { super(type, level);}
//
//    public static boolean checkDoppelgangerSpawnRules(EntityType<Doppelganger> dop, LevelAccessor accessor, MobSpawnType spawnType, BlockPos pos, RandomSource rand) {
//        return accessor.getServer().getPlayerList().getPlayerCount() > 1;
//    }
//
//    public AbstractClientPlayer getClientPlayer() { return (AbstractClientPlayer) getPlayer();}
//
//    public ServerPlayer getServerPlayer(){ return (ServerPlayer) getPlayer();}
//
//    @Override
//    protected float getStandingEyeHeight(Pose pose, EntityDimensions dim) {
//        return 1.62f;
//    }
//
//    @Override
//    public HumanoidArm getMainArm() {
//        if(getPlayer() != null){
//            return this.getPlayer().getMainArm();
//        } else {
//            return super.getMainArm();
//        }
//    }
//
//    @Override
//    protected void registerGoals(){
//        super.registerGoals();
//        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.5, true ));
//        this.targetSelector.addGoal(0, new DopTargetGoal(this));
//    }
//
//    @Override
//    protected void playEquipSound(ItemStack stack) {}
//
//    @Override
//    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
//        if (pDamageSource == DamageSource.ON_FIRE) {
//            return SoundEvents.PLAYER_HURT_ON_FIRE;
//        } else if (pDamageSource == DamageSource.DROWN) {
//            return SoundEvents.PLAYER_HURT_DROWN;
//        } else if (pDamageSource == DamageSource.SWEET_BERRY_BUSH) {
//            return SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH;
//        } else {
//            return pDamageSource == DamageSource.FREEZE ? SoundEvents.PLAYER_HURT_FREEZE : SoundEvents.PLAYER_HURT;
//        }
//    }
//
//    @Override
//    protected SoundEvent getDeathSound() {
//        return SoundEvents.PLAYER_DEATH;
//    }
//
//    @Override
//    public boolean doHurtTarget(Entity target) {
//        if(target instanceof ServerPlayer serverPlayer && !hasPlayerUUID()) {
//            this.setInvulnerable(false);
//            this.setCustomNameVisible(true);
//            this.setPlayer((Player) target);
//            this.setCustomName(target.getName());
////              this.removeEffect(MobEffects.INVISIBILITY);
//            this.setHealth(getServerPlayer().getHealth());
//            for(EquipmentSlot equipmentslot : EquipmentSlot.values()) {
//                ItemStack itemstack = serverPlayer.getItemBySlot(equipmentslot);
//                if (!itemstack.isEmpty()) {
//                    this.setItemSlot(equipmentslot, itemstack.copy());
//                }
//            }
//            if (!serverPlayer.getActiveEffects().isEmpty()) {
//                for (MobEffectInstance mobeffectinstance : serverPlayer.getActiveEffects()) {
//                    this.addEffect(mobeffectinstance);
//                }
//            }
//            this.setArrowCount(serverPlayer.getArrowCount());
//            this.setStingerCount(serverPlayer.getStingerCount());
//        }
//        return super.doHurtTarget(target);
//    }
//
//    @Override
//    public void dropAllDeathLoot(DamageSource source) {
//        if(hasPlayerUUID()) {
//            getServerPlayer().setGameMode(GameType.SURVIVAL);
//            getServerPlayer().hurt(source,Float.MAX_VALUE);
//        }
//    }
//
//    private void moveCloak() {
//        this.xCloakO = this.xCloak;
//        this.yCloakO = this.yCloak;
//        this.zCloakO = this.zCloak;
//        double d0 = this.getX() - this.xCloak;
//        double d1 = this.getY() - this.yCloak;
//        double d2 = this.getZ() - this.zCloak;
//        double d3 = 10.0D;
//        if (d0 > d3) {
//            this.xCloak = this.getX();
//            this.xCloakO = this.xCloak;
//        }
//
//        if (d2 > d3) {
//            this.zCloak = this.getZ();
//            this.zCloakO = this.zCloak;
//        }
//
//        if (d1 > d3) {
//            this.yCloak = this.getY();
//            this.yCloakO = this.yCloak;
//        }
//
//        if (d0 < -d3) {
//            this.xCloak = this.getX();
//            this.xCloakO = this.xCloak;
//        }
//
//        if (d2 < -d3) {
//            this.zCloak = this.getZ();
//            this.zCloakO = this.zCloak;
//        }
//
//        if (d1 < -d3) {
//            this.yCloak = this.getY();
//            this.yCloakO = this.yCloak;
//        }
//
//        this.xCloak += d0 * 0.25D;
//        this.zCloak += d2 * 0.25D;
//        this.yCloak += d1 * 0.25D;
//    }
//
//    @Override
//    public void tick() {
//        Player player = this.getPlayer();
//        if(this.hasPlayerUUID()){
//            if(player instanceof ServerPlayer && this.isAlive()) {
//                if (!player.isSpectator()) {
//                    getServerPlayer().setGameMode(GameType.SPECTATOR);
//                    if(((ServerPlayer) player).hasDisconnected()) {
//                        this.discard();
//                    }
//                }
//                getServerPlayer().attack(this);
//            }
//        }
//        if (getTarget()!=null && this.distanceTo(getTarget())>15){
//            this.teleportInFrontOf(this, this.getTarget());
//        }
//        this.moveCloak();
//        super.tick();
//    }
//
//    public static class DopTargetGoal extends NearestAttackableTargetGoal<Player> {
//        private final Doppelganger dop;
//        @Nullable
//        private Player pendingTarget;
//        private int teleportTime, aggroTime;
//
//        public DopTargetGoal(Doppelganger lurker) {
//            super(lurker, Player.class, false);
//            this.dop = lurker;
//        }
//
//        public void start() {
//            this.aggroTime = this.adjustedTickDelay(5);
//            this.teleportTime = 0;
//            super.start();
//        }
//
//        public void stop() {
//            this.pendingTarget = null;
//            super.stop();
//        }
//
//        public void tick() {
//            if (this.dop.getTarget() == null) {
//                super.setTarget(null);
//            }
//            if (this.pendingTarget != null && --this.aggroTime <= 0) {
//                this.target = this.pendingTarget;
//                this.pendingTarget = null;
//                super.start();
//            } else {
//                if (this.target != null && this.target.distanceToSqr(this.dop) > 256.0D
//                        && this.teleportTime++ >= this.adjustedTickDelay(30) && this.dop.teleportInFrontOf(this.dop, this.target)) {
//                    this.teleportTime = 0;
//                }
//                super.tick();
//            }
//        }
//    }
//}
