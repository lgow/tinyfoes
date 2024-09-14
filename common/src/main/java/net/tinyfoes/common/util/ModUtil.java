package net.tinyfoes.common.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tinyfoes.common.CommonTinyFoes;
import net.tinyfoes.common.items.ModItems;
import net.tinyfoes.common.registry.ModEffects;

import java.util.List;

import static net.minecraft.world.item.alchemy.PotionContents.createItemStack;
import static net.tinyfoes.common.registry.ModEffects.BABYFICATION_POTION;

public class ModUtil {
	public final static ItemStack TINY_TAB_ICON = new ItemStack(ModItems.PACIFIER.get());
	public final static List<ItemStack> TAB_ITEM_LIST = List.of(ModItems.THE_BABYFIER.get().getDefaultInstance(),
			createItemStack(Items.POTION, BABYFICATION_POTION),
			createItemStack(Items.SPLASH_POTION, ModEffects.BABYFICATION_POTION),
			createItemStack(Items.LINGERING_POTION, ModEffects.BABYFICATION_POTION),
			createItemStack(Items.TIPPED_ARROW, ModEffects.BABYFICATION_POTION));
	public final static List<Item> TAB_EGG_LIST = List.of(Items.BLAZE_SPAWN_EGG, Items.CREEPER_SPAWN_EGG,
			Items.CAVE_SPIDER_SPAWN_EGG, Items.ENDERMAN_SPAWN_EGG, Items.EVOKER_SPAWN_EGG, Items.GHAST_SPAWN_EGG,
			Items.IRON_GOLEM_SPAWN_EGG, Items.PHANTOM_SPAWN_EGG, Items.PIGLIN_BRUTE_SPAWN_EGG, Items.PILLAGER_SPAWN_EGG,
			Items.RAVAGER_SPAWN_EGG, Items.SKELETON_SPAWN_EGG, Items.SNOW_GOLEM_SPAWN_EGG, Items.SPIDER_SPAWN_EGG,
			Items.STRAY_SPAWN_EGG, Items.VINDICATOR_SPAWN_EGG, Items.WARDEN_SPAWN_EGG, Items.WANDERING_TRADER_SPAWN_EGG,
			Items.WITCH_SPAWN_EGG, Items.WITHER_SKELETON_SPAWN_EGG);

	public static ResourceLocation location(String pPath) {
		return ResourceLocation.fromNamespaceAndPath(CommonTinyFoes.MODID, pPath);
	}

	public static void babyfyModel(Iterable<ModelPart> headParts, Iterable<ModelPart> bodyParts, float headY, float headZ, PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay) {
		babyfyModel(headParts, bodyParts, headY, headZ, 1.5F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
	}

	public static void babyfyModel(Iterable<ModelPart> headParts, Iterable<ModelPart> bodyParts, float headY, float headZ, float bodyY, PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay) {
		babyfyModel(headParts, bodyParts, 0.75F, 0.5F, headY, headZ, bodyY, pPoseStack, pBuffer, pPackedLight,
				pPackedOverlay);
	}

	public static void babyfyModel(Iterable<ModelPart> headParts, Iterable<ModelPart> bodyParts, float headScale, float bodyScale, float headY, float headZ, float bodyY, PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay) {
		pPoseStack.pushPose();
		pPoseStack.scale(headScale, headScale, headScale);
		pPoseStack.translate(0.0F, headY / 16.0F, headZ / 16.0F);
		headParts.forEach((modelPart) -> {
			modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
		});
		pPoseStack.popPose();
		pPoseStack.pushPose();
		pPoseStack.scale(bodyScale, bodyScale, bodyScale);
		pPoseStack.translate(0.0F, bodyY, 0.0F);
		bodyParts.forEach((modelPart) -> {
			modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
		});
		pPoseStack.popPose();
	}
}
