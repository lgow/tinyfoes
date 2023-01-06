package com.lgow.endofherobrine.entity.herobrine;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.config.ModConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Builder extends AbstractHerobrine {
    private final String[] LETTERS = {"b", "e", "h", "i", "n", "o", "r"};
    private final String[] TRAPS = {"lantern"};
    private final String suffix = ModConfigs.LEGACY_STRUCTURES.get() ? "_legacy" : "";
    private int buildTimer;

    public Builder(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean teleportConditions(LivingEntity entity, BlockPos.MutableBlockPos mutablePos) {
        return (mutablePos.getY() > this.level.getMinBuildHeight() && !this.level.getBlockState(mutablePos).getMaterial().blocksMotion())
                || this.level.getBlockState(mutablePos.below()).getBlock() instanceof LeavesBlock;
    }

    public void teleportToSee(Player player) {
        if (!this.level.isClientSide && this.isAlive() && player!=null) {
            double randX = this.random.nextInt(40) + 10;
            double randZ = this.random.nextInt(40) + 10;
            double x = player.getX() + (this.random.nextBoolean() ? randX : -randX);
            double y = player.getY() + this.random.nextInt(16);
            double z = player.getZ() + (this.random.nextBoolean() ? randZ : -randZ);
            this.attemptTeleport(this, x, y, z, player);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if(!this.isLookingAtAnyPlayer()){ this.teleportToSee(this.getNearestPlayer());}
        this.teleportTimer = 5;
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public void placeStructure(String resLoc, int yOffset){
        if(this.getCommandSenderWorld() instanceof ServerLevel server) {
            BlockPos pos = this.blockPosition().offset(0, yOffset,0);
            StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(Rotation.getRandom(random)).setMirror(Mirror.NONE);
            StructureTemplate structure = server.getStructureManager().getOrCreate(new ResourceLocation(Main.MOD_ID + suffix, resLoc));
            structure.placeInWorld(server, pos, pos, settings, random, 2);
            this.discard();
        }
    }

    private void selectStructure(String[] structures, int yOffset){
        int rand = random.nextInt(structures.length);
        this.placeStructure(structures[rand], yOffset);
    }

    private void build(){
        if(this.isLookingAtAnyPlayer()){
            if (this.level.canSeeSky(blockPosition()) && this.distanceTo(getNearestPlayer()) >= 20) {
                if(this.random.nextInt(5) != 0){
                    this.selectStructure(LETTERS,0);
                }else {
                    this.selectStructure(TRAPS,-1);
                }
            } else {
                buildSign();
            }
        }
    }

    private void buildSign(){
        SignText signText = SignText.getRandomText(random);

        level.setBlockAndUpdate(this.blockPosition(), Blocks.OAK_SIGN.defaultBlockState().setValue(StandingSignBlock.ROTATION,
                Mth.floor((double) ((180f + this.getYRot()) * 16.0F / 360.0F)) & 15));
        SignBlockEntity existingBlockEntity = (SignBlockEntity) level.getExistingBlockEntity(this.blockPosition());

        for (int line : signText.lines) {
            existingBlockEntity.setMessage(line, Component.translatable("sign."+ signText.prefix + line));
        }
        this.discard();
    }

    @Override
    public void aiStep() {
        if(++buildTimer <= 100){
            if(buildTimer == 100) { this.build();}
        }
        super.aiStep();
    }

    enum SignText {
        LEAVE("leave",1, 2),
        STOP("stop", 1),
        WATCHING("watching",1),
        WELCOME("welcome",1, 2);

        private final String prefix;
        private int[] lines;

        SignText(String name, int... lines){
            this.prefix = name;
            this.lines = lines;
        }

        private static final List<SignText> VALUES = Collections.unmodifiableList(Arrays.asList(values()));

        public static SignText getRandomText(RandomSource random)  {
            return VALUES.get(random.nextInt(VALUES.size()));
        }
    }
}
