package com.lgow.endofherobrine.event;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.herobrine.Lurker;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lgow.endofherobrine.ModUtil.*;
import static com.lgow.endofherobrine.enchantment.EnchantmentInit.BLESSING;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class Events {

    //Triggers when the players kill mobs
    //todo prevent cheesing
    @SubscribeEvent
    public void onKillScore(LivingDeathEvent event) {
        LivingEntity target = event.getEntity();
        Entity entity = target.lastHurtByPlayerTime <= 0 ? target.lastHurtByPlayer : event.getSource().getEntity();
        if(entity instanceof Player player && !(player.getMainHandItem().getAllEnchantments().containsKey(BLESSING.get()) && rNG(10))) {
            if(!target.getType().getCategory().isFriendly()) {
                awardScoreboardValue(player, "KilledMobs", 2);
            }else{
                awardScoreboardValue(player, "KilledMobs", 4);
            }
        }
    }

    //Triggers when players attack entities
    @SubscribeEvent
    public void onMobHurt(LivingHurtEvent event){
        LivingEntity target = event.getEntity();
        Level level = target.level;
        Entity attacker = event.getSource().getEntity();
        if(event.getAmount() < target.getHealth() && attacker instanceof Player) {
            target.lastHurtByPlayerTime = 600;
            if(rNG(10)) {
                mobPossession(target, level, true);
            }
        }
    }

    //Triggers when players break blocks
    @SubscribeEvent
    public void onBreakScore(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        awardScoreboardValue(player, "BlocksChanged", 1);
    }

    //Triggers when players place blocks
    @SubscribeEvent
    public void onPlaceScore(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if(entity instanceof Player player) {
            awardScoreboardValue(player, "BlocksChanged", 1);
        }
    }

    @SubscribeEvent
    public void onEntitySpawn(LivingSpawnEvent event){
        LivingEntity living = event.getEntity();
        LevelAccessor accessor = event.getLevel();
        if(accessor instanceof Level level && living.tickCount<=1 && probability(level, 50, 15)) {
            mobPossession(living, level, false);
        }
    }

    //Triggers when mobs spawn
    @SubscribeEvent
    public void onEntitySpawning(EntityJoinLevelEvent event){
        Level level = event.getLevel();
        if(event.getEntity() instanceof ArmorStand armorStand){
            armorStand.setShowArms(true);
        }
    }

    @SubscribeEvent
    //todo timer
    public void onDeepMining(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Level level = player.level;
        Direction facing = player.getDirection();
        Lurker lurker = EntityInit.LURKER.get().create(level);
        if (!level.isClientSide && player.getY() < 30 && probability(level, 100, 100)) {
            spawnHerobrine(lurker, level, facing.getOpposite(), player.position(), 6);
            if(lurker.hasLineOfSight(player)) {
                player.sendSystemMessage(Component.translatable("whisper.behind")
                        .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
            }
        }
    }
}
