package com.lgow.tinyallies.client.model;

import com.google.common.collect.ImmutableList;
import com.lgow.tinyallies.entity.BabyCreeper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class BabyCreeperModel extends AgeableListModel<BabyCreeper> {
	private final ModelPart head, body, rightHindLeg, leftHindLeg, rightFrontLeg, leftFrontLeg;

	private boolean isSitting;

	public BabyCreeperModel(ModelPart pRoot) {
		super(true, 14.0F, 0.0F, 2F, 2.0F, 24.0F);
		this.head = pRoot.getChild("head");
		this.body = pRoot.getChild("body");
		this.leftHindLeg = pRoot.getChild("right_hind_leg");
		this.rightHindLeg = pRoot.getChild("left_hind_leg");
		this.leftFrontLeg = pRoot.getChild("right_front_leg");
		this.rightFrontLeg = pRoot.getChild("left_front_leg");
	}

	public static LayerDefinition createBodyLayer() {
		CubeDeformation pCubeDeformation = CubeDeformation.NONE;
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, pCubeDeformation),
				PartPose.offset(0.0F, 6.0F, 0.0F));
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16)
				.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, pCubeDeformation), PartPose.offset(0.0F, 6.0F, 0.0F));
		CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F,
				4.0F, pCubeDeformation);
		partdefinition.addOrReplaceChild("right_hind_leg", cubelistbuilder, PartPose.offset(-2.0F, 18.0F, 4.0F));
		partdefinition.addOrReplaceChild("left_hind_leg", cubelistbuilder, PartPose.offset(2.0F, 18.0F, 4.0F));
		partdefinition.addOrReplaceChild("right_front_leg", cubelistbuilder, PartPose.offset(-2.0F, 18.0F, -4.0F));
		partdefinition.addOrReplaceChild("left_front_leg", cubelistbuilder, PartPose.offset(2.0F, 18.0F, -4.0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
		//160
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {

			pPoseStack.pushPose();
			float f = 0.75F;
			pPoseStack.scale(f, f, f);
			float offsetY = this.isSitting ? 0.16F : 0;
			pPoseStack.translate(0.0F, 14 / 16.0F + offsetY, 0F);
			this.headParts().forEach((modelPart) -> {
				modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			});
			pPoseStack.popPose();
			pPoseStack.pushPose();
			float f1 = 0.5F;
			pPoseStack.scale(f1, f1, f1);
			offsetY = this.isSitting ? 0.25F : 0;
			pPoseStack.translate(0.0F, 1.5F + offsetY, 0.0F);
			this.bodyParts().forEach((modelPart) -> {
				modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			});
			pPoseStack.popPose();
			pPoseStack.pushPose();
			float f2 = 0.5F;
			pPoseStack.scale(f1, f1, f1);
			offsetY = this.isSitting ? 0.25F : 0;
			float offsetz = this.isSitting ? 0.13F : 0;
			pPoseStack.translate(0.0F, 1.5F + offsetY, 0.0F + offsetz);
			this.frontLegs().forEach((modelPart) -> {
				modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			});
			pPoseStack.popPose();

	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body, rightHindLeg, leftHindLeg);
	}

	protected Iterable<ModelPart> frontLegs() {
		return ImmutableList.of(rightFrontLeg, leftFrontLeg);
	}

	@Override
	public void setupAnim(BabyCreeper pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.head.yRot = pNetHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = pHeadPitch * ((float) Math.PI / 180F);
		this.rightHindLeg.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
		this.leftHindLeg.xRot = Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 1.4F * pLimbSwingAmount;
		this.rightFrontLeg.xRot = Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 1.4F * pLimbSwingAmount;
		this.leftFrontLeg.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
		if (pEntity.isInSittingPose()) {
			this.rightFrontLeg.xRot += (-(float) Math.PI / 2F);
			this.leftFrontLeg.xRot += (-(float) Math.PI / 2F);
			this.rightHindLeg.xRot = -1.565F;
			this.rightHindLeg.yRot = (-(float) Math.PI / 4F);
			this.leftHindLeg.xRot = -1.565F;
			this.leftHindLeg.yRot = ((float) Math.PI / 4F);
			this.isSitting = true;
		}
		else {
			this.isSitting = false;
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
