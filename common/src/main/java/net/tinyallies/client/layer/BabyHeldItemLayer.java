package net.tinyallies.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.tinyallies.entity.BabyMonster;

public class BabyHeldItemLayer <T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M> {
	private final ItemInHandRenderer itemInHandRenderer;

	public BabyHeldItemLayer(RenderLayerParent<T, M> pRenderer, ItemInHandRenderer pItemInHandRenderer) {
		super(pRenderer);
		this.itemInHandRenderer = pItemInHandRenderer;
	}

	public void render(PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		boolean isRightHanded = pLivingEntity.getMainArm() == HumanoidArm.RIGHT;
		ItemStack leftHandItem = isRightHanded ? pLivingEntity.getOffhandItem() : pLivingEntity.getMainHandItem();
		ItemStack rightHandItem = isRightHanded ? pLivingEntity.getMainHandItem() : pLivingEntity.getOffhandItem();
		if ((!leftHandItem.isEmpty() || !rightHandItem.isEmpty()) && pLivingEntity instanceof BabyMonster baby) {
			poseStack.pushPose();
			float offsetX = rightHandItem.getItem() instanceof BowItem ? 0.03F : 0.11F;
			poseStack.translate(offsetX, 0.75, 0);
			this.renderArmWithItem(pLivingEntity, rightHandItem, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
					HumanoidArm.RIGHT, poseStack, pBuffer, pPackedLight);
			this.renderArmWithItem(pLivingEntity, leftHandItem, ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND,
					HumanoidArm.LEFT, poseStack, pBuffer, pPackedLight);
			poseStack.popPose();
		}
	}

	protected void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, ItemTransforms.TransformType context, HumanoidArm hand, PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight) {
		if (!itemStack.isEmpty()) {
			if (itemStack.getItem() instanceof BowItem && livingEntity instanceof BabyMonster baby
					&& baby.isInSittingPose()) {
				poseStack.pushPose();
				boolean flag = hand == HumanoidArm.LEFT;
				poseStack.mulPose(Vector3f.ZP.rotationDegrees(80));
				poseStack.translate(0.355F, 0F, 0.350F);
				poseStack.translate(0F, 0.195F, -0.625F);
				this.itemInHandRenderer.renderItem(livingEntity, itemStack, context, flag, poseStack, pBuffer,
						pPackedLight);
				poseStack.popPose();
			}
			else {
				poseStack.pushPose();
				this.getParentModel().translateToHand(hand, poseStack);
				poseStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
				poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
				boolean isLeftHand = hand == HumanoidArm.LEFT;
				poseStack.translate((float) (isLeftHand ? -1 : 1) / 16.0F, 0.125F, -0.625F);
				poseStack.translate(-0.075F, 0.0F, 0.35F);
				this.itemInHandRenderer.renderItem(livingEntity, itemStack, context, isLeftHand, poseStack, pBuffer,
						pPackedLight);
				poseStack.popPose();
			}
		}
	}
}
