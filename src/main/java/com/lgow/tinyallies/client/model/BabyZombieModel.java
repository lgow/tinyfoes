package com.lgow.tinyallies.client.model;

import com.lgow.tinyallies.entity.BabyZombie;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;

public class BabyZombieModel extends ZombieModel<BabyZombie> {
	private boolean isSitting;

	public BabyZombieModel(ModelPart pRoot) {
		super(pRoot);
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		if (this.young) {
			pPoseStack.pushPose();
			float f = 0.75F;
			pPoseStack.scale(f, f, f);
				float offsetY = this.isSitting ? 0.411F : 0;
				pPoseStack.translate(0.0F, 1F + offsetY, 0);
				this.headParts().forEach((modelPart) -> {
					modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
				});
			pPoseStack.popPose();
			pPoseStack.pushPose();
				float f1 = 1.0F / 2;
				pPoseStack.scale(f1, f1, f1);
				offsetY = this.isSitting ? 0.62F : 0;
				pPoseStack.translate(0.0F, 1.5F + offsetY, 0.0F);
				this.bodyParts().forEach((modelPart) -> {
					modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
				});
			pPoseStack.popPose();
		}
	}


	@Override
	public void setupAnim(BabyZombie pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		if (pEntity.isInSittingPose()) {
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
