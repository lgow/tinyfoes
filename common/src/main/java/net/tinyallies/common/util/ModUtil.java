package net.tinyallies.common.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tinyallies.common.items.ModItems;

public class ModUtil {
	public final static ItemStack TAB_ICON = new ItemStack(ModItems.TINY_TAB.get());
	public final static NonNullList<Item> TAB_ITEM_LIST = NonNullList.of(null, ModItems.BABYFIER.get().asItem(),
			Items.BLAZE_SPAWN_EGG, Items.CREEPER_SPAWN_EGG, Items.CAVE_SPIDER_SPAWN_EGG, Items.ENDERMAN_SPAWN_EGG,
			Items.EVOKER_SPAWN_EGG, Items.GHAST_SPAWN_EGG, Items.PHANTOM_SPAWN_EGG, Items.PIGLIN_BRUTE_SPAWN_EGG,
			Items.PILLAGER_SPAWN_EGG, Items.RAVAGER_SPAWN_EGG, Items.SKELETON_SPAWN_EGG, Items.SPIDER_SPAWN_EGG,
			Items.STRAY_SPAWN_EGG, Items.VINDICATOR_SPAWN_EGG, Items.WARDEN_SPAWN_EGG, Items.WITCH_SPAWN_EGG,
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
}
