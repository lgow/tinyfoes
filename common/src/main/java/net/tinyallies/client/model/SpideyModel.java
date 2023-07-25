package net.tinyallies.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelPart;
import net.tinyallies.entity.Spidey;
import net.tinyallies.util.ModUtil;

public class SpideyModel <T extends Spidey> extends SpiderModel<T> {
	private final ModelPart body0, body1, head, rightHindLeg, leftHindLeg, rightMiddleHindLeg, leftMiddleHindLeg, rightMiddleFrontLeg, leftMiddleFrontLeg, rightFrontLeg, leftFrontLeg;

	public SpideyModel(ModelPart modelPart) {
		super(modelPart);
		this.head = modelPart.getChild("head");
		this.body0 = modelPart.getChild("body0");
		this.body1 = modelPart.getChild("body1");
		this.rightHindLeg = modelPart.getChild("right_hind_leg");
		this.leftHindLeg = modelPart.getChild("left_hind_leg");
		this.rightMiddleHindLeg = modelPart.getChild("right_middle_hind_leg");
		this.leftMiddleHindLeg = modelPart.getChild("left_middle_hind_leg");
		this.rightMiddleFrontLeg = modelPart.getChild("right_middle_front_leg");
		this.leftMiddleFrontLeg = modelPart.getChild("left_middle_front_leg");
		this.rightFrontLeg = modelPart.getChild("right_front_leg");
		this.leftFrontLeg = modelPart.getChild("left_front_leg");
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		ModUtil.babyfyModel(headParts(), bodyParts(), 11F, 1F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed,
				pGreen, pBlue, pAlpha);
	}

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		if (pEntity.isInSittingPose()) {
			this.rightHindLeg.zRot = (-(float) Math.PI / 6.5F);
			this.rightMiddleHindLeg.zRot = (-(float) Math.PI / 8.5F);
			this.rightMiddleFrontLeg.zRot = (-(float) Math.PI / 8.5F);
			this.rightFrontLeg.zRot = (-(float) Math.PI / 6.5F);
			this.leftHindLeg.zRot = ((float) Math.PI / 6.5F);
			this.leftMiddleHindLeg.zRot = ((float) Math.PI / 8.5F);
			this.leftMiddleFrontLeg.zRot = ((float) Math.PI / 8.5F);
			this.leftFrontLeg.zRot = ((float) Math.PI / 6.5F);
		}
	}

	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head);
	}

	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(rightHindLeg, leftHindLeg, rightMiddleHindLeg, leftMiddleHindLeg, rightMiddleFrontLeg,
				leftMiddleFrontLeg, rightFrontLeg, leftFrontLeg, body0, body1);
	}
}
