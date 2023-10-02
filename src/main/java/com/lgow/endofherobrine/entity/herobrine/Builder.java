package com.lgow.endofherobrine.entity.herobrine;

import com.lgow.endofherobrine.block.BlockInit;
import com.lgow.endofherobrine.config.ModConfigs;
import com.lgow.endofherobrine.util.ModResourceLocation;
import com.lgow.endofherobrine.world.data.ModSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Builder extends AbstractHerobrine {
	private final String[] TRAPS = { "lantern" };
	private final String string = "herobrine";

	private final String directory = ModConfigs.shouldBuildLegacyStructures() ? "legacy/" : "";

	private int buildTimer;

	public Builder(EntityType<? extends PathfinderMob> type, Level level) {
		super(type, level);
	}

	@Override
	public boolean checkSafePos(LivingEntity entity, BlockPos.MutableBlockPos mutablePos, boolean avoidwater) {
		return (mutablePos.getY() > this.level().getMinBuildHeight() && !this.level().getBlockState(mutablePos)
				.blocksMotion()) || this.level().getBlockState(mutablePos).is(BlockTags.LEAVES);
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
		if (!this.canSeeAnyPlayers()) { this.tpToWatchPlayer(this.getNearestPlayer()); }
		this.teleportCooldown = 5;
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}

	private void placeStructure(String resLoc, int yOffset) {
		if (this.getCommandSenderWorld() instanceof ServerLevel server) {
			boolean canBuild = true;
			BlockPos pos = this.blockPosition().offset(0, yOffset, 0);
			StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(Rotation.getRandom(random))
					.setMirror(Mirror.NONE);
			StructureTemplate structure = server.getStructureManager().getOrCreate(
					new ModResourceLocation(directory + resLoc));
			for (BlockState state : server.getBlockStates(AABB.of(structure.getBoundingBox(settings, pos))).toList()) {
				if (state.getTags().toList().contains(BlockInit.NO_OVERRIDE)) {
					canBuild = false;
				}
			}
			if (!canBuild) {
				this.tpToWatchPlayer(this.getNearestPlayer());
				this.buildTimer = 0;
			}
			else {
				structure.placeInWorld(server, pos, pos, settings, random, 2);
				this.discard();
			}
		}
	}

	private String getLetterToBuild(int lastLetterIndex){
		return this.getName().getString().toLowerCase().substring(lastLetterIndex, lastLetterIndex + 1);
	}

	private void selectStructure(String[] structures, int yOffset) {
		int rand = random.nextInt(structures.length);
		this.placeStructure(structures[rand], yOffset);
	}

	private void build() {
		if (this.canSeeAnyPlayers() && this.getNearestPlayer() != null) {
			if (this.level().canSeeSky(blockPosition()) && this.distanceTo(getNearestPlayer()) >= 10) {
				if (this.random.nextInt(5) != 0) {
					this.placeStructure(getLetterToBuild(ModSavedData.get(this.getServer()).getLastLetterIndex()), 0);
					ModSavedData.get(this.getServer()).updateLastLetterIndex();
				}
				else {
					this.selectStructure(TRAPS, -1);
				}
			}
			else {
				buildSign();
			}
		}
	}

	private void buildSign() {
		BuilderMessage signText = BuilderMessage.getRandomText(random);
		BlockPos pos = this.blockPosition();
		//todo fix sign direction & sign type by biome & sign breaking consequences
		level().setBlockAndUpdate(pos, Blocks.OAK_SIGN.defaultBlockState()
				.setValue(StandingSignBlock.ROTATION, Mth.floor(this.getXRot()) & 15));
		level().playSound(this, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 0.5F,
				(float) (0.8F + (Math.random() * 0.2D)));
		SignBlockEntity existingBlockEntity = (SignBlockEntity) level().getExistingBlockEntity(pos);
		SignText text = new SignText();
		for (int line : signText.lines) {
			text = text.setMessage(line,Component.translatable("sign." + signText.prefix + line));
		}
		existingBlockEntity.setText(text, true);
		existingBlockEntity.setText(text,false);
		this.discard();
	}

	@Override
	public void customServerAiStep() {
		if (++buildTimer >= 100) {
			this.build();
		}
		super.customServerAiStep();
	}

	private enum BuilderMessage {
		LEAVE("leave", 1, 2), STOP("stop", 1), WATCHING("watching", 1), WELCOME("welcome", 1, 2);

		private static final List<BuilderMessage> VALUES = List.of(values());

		private final String prefix;

		private final int[] lines;

		BuilderMessage(String name, int... lines) {
			this.prefix = name;
			this.lines = lines;
		}

		private static BuilderMessage getRandomText(RandomSource random) {
			return VALUES.get(random.nextInt(VALUES.size()));
		}
	}
}
