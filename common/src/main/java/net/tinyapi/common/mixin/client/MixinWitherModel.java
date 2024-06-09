package net.tinyapi.common.mixin.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.WitherBossModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.tinyapi.common.util.ModUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WitherBossModel.class)
public abstract class MixinWitherModel <T extends WitherBoss> extends HierarchicalModel<T> {
	@Shadow @Final private ModelPart centerHead, rightHead, leftHead, ribcage, tail, root;

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		if (this.young) {
			ModUtil.babyfyModel(headParts(), bodyParts(), 15F, 0F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay,
					pRed, pGreen, pBlue, pAlpha);
			pPoseStack.pushPose();
			pPoseStack.scale(0.75F, 0.75F, 0.75F);
			pPoseStack.translate(0.0F, 15 / 16.0F, 0 / 16.0F);
			pPoseStack.translate(0.06F, -0.065, 0);
			rightHead.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			pPoseStack.popPose();
			pPoseStack.pushPose();
			pPoseStack.scale(0.75F, 0.75F, 0.75F);
			pPoseStack.translate(0.0F, 15 / 16.0F, 0 / 16.0F);
			pPoseStack.translate(-0.06F, -0.065, 0);
			leftHead.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			pPoseStack.popPose();
		}
		else {
			super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		}
	}

	@Unique
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(centerHead);
	}

	@Unique
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(ribcage, tail, root.getChild("shoulders"));
	}
}
