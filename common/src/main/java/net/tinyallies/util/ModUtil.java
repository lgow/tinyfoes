package net.tinyallies.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.tinyallies.entity.BabyCreeper;
import net.tinyallies.entity.BabyEnderman;
import net.tinyallies.entity.ModEntities;

import java.util.Map;

public class ModUtil {
	private static final Map<EntityType<? extends PathfinderMob>, EntityType<? extends PathfinderMob>> bayficationList = Map.ofEntries(
			Map.entry(EntityType.CREEPER, ModEntities.CREEPY.get()),
			Map.entry(EntityType.SKELETON, ModEntities.SKELLY.get()),
			Map.entry(EntityType.ENDERMAN, ModEntities.ENDERBOY.get()),
			Map.entry(EntityType.SPIDER, ModEntities.SPIDEY.get()),
			Map.entry(EntityType.ZOMBIE, ModEntities.ZOMBY.get()));

	public static void babifyMob(Mob entityIn) {
		if (bayficationList.containsKey(entityIn.getType())) {
			Mob baby = entityIn.convertTo(bayficationList.get(entityIn.getType()), true);
			baby.setHealth(entityIn.getHealth());
			if (baby instanceof BabyCreeper creeper) {
				creeper.setPowered(((Creeper) entityIn).isPowered());
				creeper.setSwellDir(((Creeper) entityIn).getSwellDir());
			}
			else if (baby instanceof BabyEnderman enderman) {
				enderman.setCarriedBlock(((EnderMan) entityIn).getCarriedBlock());
				enderman.setTarget(entityIn.getTarget());
			}
		}
	}

	public static void babyfyModel(Iterable<ModelPart> headParts, Iterable<ModelPart> bodyParts, float headY, float headZ, PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		pPoseStack.pushPose();
		pPoseStack.scale(0.75F, 0.75F, 0.75F);
		pPoseStack.translate(0.0F, headY / 16.0F, headZ / 16.0F);
		headParts.forEach((modelPart) -> {
			modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		});
		pPoseStack.popPose();
		pPoseStack.pushPose();
		pPoseStack.scale(0.5F, 0.5F, 0.5F);
		pPoseStack.translate(0.0F, 1.5F, 0.0F);
		bodyParts.forEach((modelPart) -> {
			modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		});
		pPoseStack.popPose();
	}
}
