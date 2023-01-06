package com.lgow.endofherobrine;

import com.lgow.endofherobrine.entity.herobrine.AbstractHerobrine;
import com.lgow.endofherobrine.entity.possessed.animal.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.lgow.endofherobrine.entity.EntityInit.*;
import static com.lgow.endofherobrine.entity.ModMobTypes.POSSESSED;

public class ModUtil {

    public static boolean rNG(int bound){
        return new Random().nextInt(bound) == 0;
    }

    public static Score score(Level level, String player, String Objective){
        Scoreboard scoreboard = level.getScoreboard();
        return scoreboard.getOrCreatePlayerScore(player, Objects.requireNonNull(scoreboard.getObjective(Objective)));
    }

    public static boolean destructionScore(Level level, int bound){
        //addme destruction balacement
        return score(level, "Total","Destruction").getScore() > bound;
    }

    public static boolean canSpawnHerobrine(Level level, BlockPos pos){
        AABB aabb = new AABB(pos).inflate(256);
        List<AbstractHerobrine> list = level.getEntitiesOfClass(AbstractHerobrine.class, aabb);
        return ModUtil.destructionScore(level,1) && list.isEmpty();
    }

    public static boolean canSpawnPosCreature(LevelAccessor accessor){
        //addme destruction balacement
        return accessor instanceof Level level ? destructionScore(level, 20) : false;
    }

    public static boolean canSpawnPosMonster(LevelAccessor accessor){
        //addme destruction balacement
        return accessor instanceof Level level ? destructionScore(level, 40) : false;
    }

    public static boolean probability(Level level, int score, int rNG){
        return destructionScore(level, score) && rNG(rNG);
    }

    public static void awardScoreboardValue(Player player, String objective, int value){
        score(player.level, player.getScoreboardName(), objective).add(value);
    }

    public static void convertEntity(LivingEntity mobIn, Mob posMob, Level level, boolean shouldBeAngry){
        posMob.setBaby(mobIn.isBaby());
        posMob.setCustomName(mobIn.getCustomName());
        posMob.copyPosition(mobIn);
        for(EquipmentSlot equipmentslot : EquipmentSlot.values()) {
            ItemStack itemstack = mobIn.getItemBySlot(equipmentslot);
            if (!itemstack.isEmpty()) {
                posMob.setItemSlot(equipmentslot, itemstack.copy());
                itemstack.setCount(0);
            }
        }
        if (!mobIn.getActiveEffects().isEmpty()) {
            for (MobEffectInstance mobeffectinstance : mobIn.getActiveEffects()) {
                posMob.addEffect(mobeffectinstance);
            }
        }
        level.addFreshEntity(posMob);
        mobIn.discard();
        if(shouldBeAngry && posMob instanceof NeutralMob neutral){
            level.playSound(null, mobIn.blockPosition(), SoundEvents.GHAST_HURT,
                    SoundSource.HOSTILE, 1.0F, 1.0F);
            neutral.setLastHurtByMob(mobIn.getLastHurtByMob());
        }
    }

    public static void mobPossession(LivingEntity entityIn, Level level, boolean shouldBeAngry){
        if(entityIn.getMobType() != POSSESSED) {
            if (entityIn instanceof Chicken) {
                PosChicken posMob = POS_CHICKEN.get().create(level);
                convertEntity(entityIn, posMob, level, shouldBeAngry);
            }
            else if (entityIn instanceof Cow) {
                PosCow posMob = POS_COW.get().create(level);
                convertEntity(entityIn, posMob, level, shouldBeAngry);
            }
            else if (entityIn instanceof Pig pig) {
                PosPig posMob = POS_PIG.get().create(level);
                if (pig.isSaddled()) posMob.equipSaddle(null);
                convertEntity(entityIn, posMob, level, shouldBeAngry);
            }
            else if (entityIn instanceof Rabbit rabbit) {
                PosRabbit posMob = POS_RABBIT.get().create(level);
                posMob.setVariant(rabbit.getVariant());
                convertEntity(entityIn, posMob, level, shouldBeAngry);
            }
            else if (entityIn instanceof Sheep sheep) {
                PosSheep posMob = POS_SHEEP.get().create(level);
                posMob.setColor(sheep.getColor());
                posMob.setSheared(sheep.isSheared());
                convertEntity(entityIn, posMob, level, shouldBeAngry);
            }
            else if (entityIn instanceof Villager villager) {
                PosVillager posMob = POS_VILLAGER.get().create(level);
                posMob.setVillagerData(villager.getVillagerData());
                convertEntity(entityIn, posMob, level, shouldBeAngry);
            }
        }
    }

    public static void spawnHerobrine(AbstractHerobrine herobrine, Level level, Direction facing, Vec3 pos, double offs) {
        switch (facing) {
            case NORTH -> herobrine.setPos(pos.subtract(0,0,offs));
            case SOUTH -> herobrine.setPos(pos.add(0,0,offs));
            case EAST -> herobrine.setPos(pos.add(offs,0,0));
            case WEST -> herobrine.setPos(pos.subtract(offs,0,0));
        }
        level.addFreshEntity(herobrine);
    }
}
