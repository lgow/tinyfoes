package net.tinyallies.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.tinyallies.entity.Creepy;
import net.tinyallies.util.ModUtil;

public class CreepyModel extends CreeperModel<Creepy> {
	private final ModelPart head, body, rightHindLeg, leftHindLeg, rightFrontLeg, leftFrontLeg;
	private boolean shouldOffsetLegs;

	public CreepyModel(ModelPart pRoot) {
		super(pRoot);
		this.head = pRoot.getChild("head");
		this.body = pRoot.getChild("body");
		this.leftHindLeg = pRoot.getChild("right_hind_leg");
		this.rightHindLeg = pRoot.getChild("left_hind_leg");
		this.leftFrontLeg = pRoot.getChild("right_front_leg");
		this.rightFrontLeg = pRoot.getChild("left_front_leg");
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		ModUtil.babyfyModel(headParts(), bodyParts(), 14F, 0F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed,
				pGreen, pBlue, pAlpha);
		pPoseStack.pushPose();
		pPoseStack.scale(0.5F, 0.5F, 0.5F);
		float frontLegsOffset = this.shouldOffsetLegs ? 0.13F : 0;
		pPoseStack.translate(0.0F, 1.5F, 0.0F + frontLegsOffset);
		this.frontLegs().forEach((modelPart) -> {
			modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		});
		pPoseStack.popPose();
	}

	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head);
	}

	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body, rightHindLeg, leftHindLeg);
	}

	protected Iterable<ModelPart> frontLegs() {
		return ImmutableList.of(rightFrontLeg, leftFrontLeg);
	}

	@Override
	public void setupAnim(Creepy pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		if (pEntity.isInSittingPose()) {
			this.rightFrontLeg.xRot = (-(float) Math.PI / 2F);
			this.leftFrontLeg.xRot = (-(float) Math.PI / 2F);
			this.rightHindLeg.xRot = -1.565F;
			this.rightHindLeg.yRot = (-(float) Math.PI / 4F);
			this.leftHindLeg.xRot = -1.565F;
			this.leftHindLeg.yRot = ((float) Math.PI / 4F);
			this.shouldOffsetLegs = true;
		}
		else {
			this.shouldOffsetLegs = false;
			float f = 1.0F;
			if (pEntity.getFallFlyingTicks() > 4) {
				f = (float) pEntity.getDeltaMovement().lengthSqr();
				f /= 0.2F;
				f *= f * f;
			}
			if (f < 1.0F) {
				f = 1.0F;
			}
			this.rightHindLeg.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount / f;
			this.leftHindLeg.xRot = Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 1.4F * pLimbSwingAmount / f;
			this.rightHindLeg.yRot = 0.005F;
			this.leftHindLeg.yRot = -0.005F;
			this.rightHindLeg.zRot = 0.005F;
			this.leftHindLeg.zRot = -0.005F;
		}
	}
}
