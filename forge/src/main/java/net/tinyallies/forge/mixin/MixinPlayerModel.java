package net.tinyallies.forge.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tinyallies.common.util.ModUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@OnlyIn(Dist.CLIENT)
@Mixin(PlayerModel.class)
public abstract class MixinPlayerModel <T extends LivingEntity> extends HumanoidModel<T> {
	@Shadow @Final public ModelPart jacket, leftSleeve, rightSleeve, rightPants, leftPants;

	public MixinPlayerModel(ModelPart modelPart) {
		super(modelPart);
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		if (this.young) {
			ModUtil.babyfyModel(headParts(), bodyParts(), 16F, 0F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay,
					pRed, pGreen, pBlue, pAlpha);
		}
		else {
			super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		}
	}

	@Unique
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(head, hat);
	}

	@Unique
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(body, jacket, leftArm, leftSleeve, rightArm, rightSleeve, rightLeg, rightPants, leftLeg,
				leftPants);
	}

//	@Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
//	public void setupAnim(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
//		headParts().forEach((modelPart) -> {
//			if (this.young) {
//				modelPart.xScale = 1.5F;
//				modelPart.yScale = 1.5F;
//				modelPart.zScale = 1.5F;
//			}
//			else {
//				modelPart.xScale = 1.0F;
//				modelPart.yScale = 1.0F;
//				modelPart.zScale = 1.0F;
//			}
//		});
//		bodyParts().forEach((modelPart) -> {
//			if (this.young) {
//				modelPart.xScale = 0.5F;
//				modelPart.yScale = 0.5F;
//				modelPart.zScale = 0.5F;
//			}
//			else {
//				modelPart.xScale = 1.0F;
//				modelPart.yScale = 1.0F;
//				modelPart.zScale = 1.0F;
//			}
//		});
//	}
}
