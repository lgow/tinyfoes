package net.tinyfoes.common.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tinyfoes.common.items.ModItems;

import java.util.List;

public class ModUtil {
	public final static ItemStack TINY_TAB_ICON = new ItemStack(ModItems.TINY_ICON.get());
	public final static List<Item> TAB_ITEM_LIST = List.of(ModItems.BABYFIER.get(),
// doesnt work
//			PotionUtils.setPotion(new ItemStack(Items.POTION), ModEffects.BABYFICATION_POTION.get()).getItem(),
//			PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), ModEffects.BABYFICATION_POTION.get()).getItem(),
//			PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), ModEffects.BABYFICATION_POTION.get()).getItem(),
			Items.BLAZE_SPAWN_EGG, Items.CREEPER_SPAWN_EGG, Items.CAVE_SPIDER_SPAWN_EGG, Items.ENDERMAN_SPAWN_EGG,
			Items.EVOKER_SPAWN_EGG, Items.GHAST_SPAWN_EGG, Items.IRON_GOLEM_SPAWN_EGG, Items.PHANTOM_SPAWN_EGG,
			Items.PIGLIN_BRUTE_SPAWN_EGG, Items.PILLAGER_SPAWN_EGG, Items.RAVAGER_SPAWN_EGG, Items.SKELETON_SPAWN_EGG,
			Items.SNOW_GOLEM_SPAWN_EGG, Items.SPIDER_SPAWN_EGG, Items.STRAY_SPAWN_EGG, Items.VINDICATOR_SPAWN_EGG,
			Items.WARDEN_SPAWN_EGG, Items.WANDERING_TRADER_SPAWN_EGG, Items.WITCH_SPAWN_EGG,
			Items.WITHER_SKELETON_SPAWN_EGG);

	public static void babyfyModel(Iterable<ModelPart> headParts, Iterable<ModelPart> bodyParts, float headY, float headZ, PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		babyfyModel(headParts, bodyParts, headY, headZ, 1.5F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed,
				pGreen, pBlue, pAlpha);
	}

	public static void babyfyModel(Iterable<ModelPart> headParts, Iterable<ModelPart> bodyParts, float headY, float headZ, float bodyY, PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		babyfyModel(headParts, bodyParts, 0.75F, 0.5F, headY, headZ, bodyY, pPoseStack, pBuffer, pPackedLight,
				pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
	}

	public static void babyfyModel(Iterable<ModelPart> headParts, Iterable<ModelPart> bodyParts, float headScale, float bodyScale, float headY, float headZ, float bodyY, PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		pPoseStack.pushPose();
		pPoseStack.scale(headScale, headScale, headScale);
		pPoseStack.translate(0.0F, headY / 16.0F, headZ / 16.0F);
		headParts.forEach((modelPart) -> {
			modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		});
		pPoseStack.popPose();
		pPoseStack.pushPose();
		pPoseStack.scale(bodyScale, bodyScale, bodyScale);
		pPoseStack.translate(0.0F, bodyY, 0.0F);
		bodyParts.forEach((modelPart) -> {
			modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		});
		pPoseStack.popPose();
	}

	public static void scaleModelPart(ModelPart part, float scale) {
		part.xScale = scale;
		part.yScale = scale;
		part.zScale = scale;
	}

	public static void scaleBodyPart(ModelPart part) {
		scaleModelPart(part, 0.75f);
	}

	public static void scaleHeadPart(ModelPart part, boolean b) {
		scaleModelPart(part, b ? 2.0f : 1.0f);
	}

	public static void scaleReset(ModelPart part) {
		scaleModelPart(part, 1.0f);
	}
}
