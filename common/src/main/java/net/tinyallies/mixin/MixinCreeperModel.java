package net.tinyallies.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.tinyallies.util.ModUtil;
import org.spongepowered.asm.mixin.*;

@Environment(EnvType.CLIENT)
@Mixin(CreeperModel.class)
public abstract class MixinCreeperModel<T extends Entity> extends HierarchicalModel<T> {
	@Mutable @Shadow @Final private ModelPart head, leftHindLeg, rightHindLeg, leftFrontLeg, rightFrontLeg, root;

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		if(this.young){
			ModUtil.babyfyModel(headParts(), bodyParts(), 14F, 0F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay,
					pRed, pGreen, pBlue, pAlpha);
			pPoseStack.pushPose();
			pPoseStack.scale(0.5F, 0.5F, 0.5F);
			pPoseStack.translate(0.0F, 1.5F, 0.0F );
			this.frontLegs().forEach((modelPart) -> {
				modelPart.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			});
			pPoseStack.popPose();
		}else {
					super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		}
	}
	@Unique
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head);
	}

	@Unique
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(root, rightHindLeg, leftHindLeg);
	}
	@Unique
	protected Iterable<ModelPart> frontLegs() {
		return ImmutableList.of(rightFrontLeg, leftFrontLeg);
	}
}
