package net.tinyallies.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.tinyallies.util.ModUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EndermanModel.class)
public class MixinEnderManModel <T extends LivingEntity> extends HumanoidModel<T> {
	@Shadow public boolean carrying, creepy;

	public MixinEnderManModel(ModelPart pRoot) {
		super(pRoot);
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		if (this.young) {
			ModUtil.babyfyModel(headParts(), bodyParts(), 20.05F, 0F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay,
					pRed, pGreen, pBlue, pAlpha);
		}
		else {
			super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
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

	@Override
	public void setupAnim(T livingEntity, float f, float g, float h, float i, float j) {
		super.setupAnim(livingEntity, f, g, h, i, j);
		this.head.visible = true;
		int k = -14;
		this.body.xRot = 0.0f;
		this.body.y = -14.0f;
		this.body.z = -0.0f;
		this.rightLeg.xRot -= 0.0f;
		this.leftLeg.xRot -= 0.0f;
		this.rightArm.xRot *= 0.5f;
		this.leftArm.xRot *= 0.5f;
		this.rightLeg.xRot *= 0.5f;
		this.leftLeg.xRot *= 0.5f;
		float l = 0.4f;
		if (this.rightArm.xRot > 0.4f) {
			this.rightArm.xRot = 0.4f;
		}
		if (this.leftArm.xRot > 0.4f) {
			this.leftArm.xRot = 0.4f;
		}
		if (this.rightArm.xRot < -0.4f) {
			this.rightArm.xRot = -0.4f;
		}
		if (this.leftArm.xRot < -0.4f) {
			this.leftArm.xRot = -0.4f;
		}
		if (this.rightLeg.xRot > 0.4f) {
			this.rightLeg.xRot = 0.4f;
		}
		if (this.leftLeg.xRot > 0.4f) {
			this.leftLeg.xRot = 0.4f;
		}
		if (this.rightLeg.xRot < -0.4f) {
			this.rightLeg.xRot = -0.4f;
		}
		if (this.leftLeg.xRot < -0.4f) {
			this.leftLeg.xRot = -0.4f;
		}
		if (carrying) {
			this.rightArm.zRot = 0.05f;
			this.leftArm.zRot = -0.05f;
			if (this.young) {
				this.rightArm.yRot += 0.41;
				this.leftArm.yRot += -0.41;
				this.rightArm.xRot = -0.7f;
				this.leftArm.xRot = -0.7f;
			}
			else {
				this.rightArm.xRot = -0.5f;
				this.leftArm.xRot = -0.5f;
			}
		}
		this.rightLeg.z = 0.0f;
		this.leftLeg.z = 0.0f;
		this.rightLeg.y = -5.0f;
		this.leftLeg.y = -5.0f;
		this.head.z = -0.0f;
		this.head.y = -13.0f;
		this.hat.x = this.head.x;
		this.hat.y = this.head.y;
		this.hat.z = this.head.z;
		this.hat.xRot = this.head.xRot;
		this.hat.yRot = this.head.yRot;
		this.hat.zRot = this.head.zRot;
		if (creepy) {
			float m = 1.0f;
			this.head.y -= 5.0f;
		}
		int n = -14;
		this.rightArm.setPos(-5.0f, -12.0f, 0.0f);
		this.leftArm.setPos(5.0f, -12.0f, 0.0f);
	}
}
