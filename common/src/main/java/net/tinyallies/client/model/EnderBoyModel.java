package net.tinyallies.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.geom.ModelPart;
import net.tinyallies.entity.EnderBoy;
import net.tinyallies.util.ModUtil;

public class EnderBoyModel <T extends EnderBoy> extends EndermanModel<T> {
	public EnderBoyModel(ModelPart pRoot) {
		super(pRoot);
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		ModUtil.babyfyModel(headParts(), bodyParts(), 20.05F, 0F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay,
				pRed, pGreen, pBlue, pAlpha);
	}

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		if (pEntity.isInSittingPose()) {
			this.rightLeg.xRot = -1.49F;
			this.leftLeg.xRot = -1.49F;
			if (this.carrying) {
				this.rightArm.xRot += (-(float) Math.PI / 3.5F);
				this.leftArm.xRot += (-(float) Math.PI / 3.5F);
				this.rightArm.yRot += ((float) Math.PI / 10.4F);
				this.leftArm.yRot += (-(float) Math.PI / 10.4F);
				this.rightLeg.yRot = ((float) Math.PI / 7.5F);
				this.leftLeg.yRot = (-(float) Math.PI / 7.5F);
			}
			else {
				this.rightArm.xRot += (-(float) Math.PI / 2.55F);
				this.leftArm.xRot += (-(float) Math.PI / 2.55F);
				this.rightLeg.yRot = ((float) Math.PI / 10F);
				this.leftLeg.yRot = (-(float) Math.PI / 10F);
			}
		}
		else {
			if (this.carrying) {
				this.rightArm.yRot += ((float) Math.PI / 6.5F);
				this.leftArm.yRot += (-(float) Math.PI / 6.5F);
			}
		}
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head, this.hat);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg);
	}
}
