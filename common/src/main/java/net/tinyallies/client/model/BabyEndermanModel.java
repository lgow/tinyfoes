package net.tinyallies.client.model;

import com.google.common.collect.ImmutableList;
import net.tinyallies.entity.BabyEnderman;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.geom.ModelPart;

public class BabyEndermanModel <T extends BabyEnderman> extends EndermanModel<T> {
	private boolean isSitting;

	public BabyEndermanModel(ModelPart pRoot) {
		super(pRoot);
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {

			pPoseStack.pushPose();
			float f = 0.75F;
			float offsetY = this.isSitting ? 1.05F : 0;
			pPoseStack.scale(f, f, f);
			pPoseStack.translate(0.0F, 1.28125F + offsetY, 0 / 16.0F);
			this.headParts().forEach((modelPart) -> {
				modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			});
			pPoseStack.popPose();
			pPoseStack.pushPose();
			float f1 = 0.5F;
			pPoseStack.scale(f1, f1, f1);
			offsetY = this.isSitting ? 1.6F : 0;
			pPoseStack.translate(0.0F, 1.5F + offsetY, 0.0F);
			this.bodyParts().forEach((modelPart) -> {
				modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			});
			pPoseStack.popPose();

	}

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		if (pEntity.isInSittingPose()) {
			if(this.carrying){
				this.rightArm.xRot += (-(float) Math.PI / 4F);
				this.leftArm.xRot += (-(float) Math.PI / 4F);
			}else{
				this.rightArm.xRot += (-(float) Math.PI / 2.55F);
				this.leftArm.xRot += (-(float) Math.PI / 2.55F);
			}
			this.rightLeg.xRot = -1.49F;
			this.rightLeg.yRot = ((float) Math.PI / 10F);
			this.leftLeg.xRot = -1.49F;
			this.leftLeg.yRot = (-(float) Math.PI / 10F);
			this.isSitting = true;
		} else{
			this.isSitting = false;
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
