package net.tinyallies.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BabyHeldItemLayer <T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M> {
	private final ItemInHandRenderer itemInHandRenderer;

	public BabyHeldItemLayer(RenderLayerParent<T, M> pRenderer, ItemInHandRenderer pItemInHandRenderer) {
		super(pRenderer);
		this.itemInHandRenderer = pItemInHandRenderer;
	}

	public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		boolean isRightHanded = pLivingEntity.getMainArm() == HumanoidArm.RIGHT;
		ItemStack mainHandItem = isRightHanded ? pLivingEntity.getOffhandItem() : pLivingEntity.getMainHandItem();
		ItemStack offHandItem = isRightHanded ? pLivingEntity.getMainHandItem() : pLivingEntity.getOffhandItem();
		if (!mainHandItem.isEmpty() || !offHandItem.isEmpty()) {
			pMatrixStack.pushPose();
			float offsetItem = offHandItem.getItem() instanceof BowItem ? 0.03F : 0.11F;
			pMatrixStack.translate(offsetItem, 0.75F, 0);
			this.renderArmWithItem(pLivingEntity, offHandItem, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
					HumanoidArm.RIGHT, pMatrixStack, pBuffer, pPackedLight);
			this.renderArmWithItem(pLivingEntity, mainHandItem, ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
					HumanoidArm.LEFT, pMatrixStack, pBuffer, pPackedLight);
			pMatrixStack.popPose();
		}
	}

	protected void renderArmWithItem(LivingEntity p_117185_, ItemStack p_117186_, ItemDisplayContext p_270970_, HumanoidArm hand, PoseStack poseStack, MultiBufferSource p_117190_, int p_117191_) {
		if (!p_117186_.isEmpty()) {
			poseStack.pushPose();
			this.getParentModel().translateToHand(hand, poseStack);
			poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
			poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
			boolean flag = hand == HumanoidArm.LEFT;
			poseStack.translate((float) (flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);
			poseStack.translate(-0.075F, 0.0F, 0.350F);
			this.itemInHandRenderer.renderItem(p_117185_, p_117186_, p_270970_, flag, poseStack, p_117190_, p_117191_);
			poseStack.popPose();
		}
	}
}
