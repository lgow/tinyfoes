package net.tinyallies.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;
import net.tinyallies.entity.Zomby;
import net.tinyallies.util.ModUtil;

public class ZombyModel extends ZombieModel<Zomby> {
	public ZombyModel(ModelPart pRoot) {
		super(pRoot);
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		ModUtil.babyfyModel(headParts(), bodyParts(), 16F, 0, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed,
				pGreen, pBlue, pAlpha);
	}

	@Override
	public void setupAnim(Zomby pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		if (pEntity.isInSittingPose()) {
			this.rightLeg.xRot = -1.565F;
			this.rightLeg.yRot = ((float) Math.PI / 10F);
			this.leftLeg.xRot = -1.565F;
			this.leftLeg.yRot = (-(float) Math.PI / 10F);
		}
	}
}
