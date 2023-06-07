package com.lgow.tinyallies.client.model;

import com.lgow.tinyallies.entity.BabySkeleton;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;

public class BabySkeletonModel extends SkeletonModel<BabySkeleton> {
	private boolean isSitting;

	public BabySkeletonModel(ModelPart pRoot) {
		super(pRoot);
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		pPoseStack.pushPose();
		float f = 0.75F;
		pPoseStack.scale(f, f, f);
		float offsetY = this.isSitting ? 0.456F : 0;
		pPoseStack.translate(0.0F, 1F + offsetY, 0);
		this.headParts().forEach((modelPart) -> {
			modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		});
		pPoseStack.popPose();
		pPoseStack.pushPose();
		float f1 = 1.0F / 2;
		pPoseStack.scale(f1, f1, f1);
		offsetY = this.isSitting ? 0.68F : 0;
		pPoseStack.translate(0.0F, 1.5F + offsetY, 0.0F);
		this.bodyParts().forEach((modelPart) -> {
			modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		});
		pPoseStack.popPose();
	}

	@Override
	public void setupAnim(BabySkeleton pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		if (pEntity.isInSittingPose()) {
			this.rightArm.xRot += (-(float) Math.PI / 5F);
			this.leftArm.xRot += (-(float) Math.PI / 5F);
			this.rightLeg.xRot = -1.565F;
			this.rightLeg.yRot = ((float) Math.PI / 10F);
			this.leftLeg.xRot = -1.565F;
			this.leftLeg.yRot = (-(float) Math.PI / 10F);
			this.isSitting = true;
		}
		else {
			this.isSitting = false;
		}
	}
}
