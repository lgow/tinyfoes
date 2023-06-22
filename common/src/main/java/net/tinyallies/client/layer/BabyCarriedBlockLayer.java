package net.tinyallies.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import net.tinyallies.client.model.BabyEndermanModel;
import net.tinyallies.entity.BabyEnderman;

//@OnlyIn(Dist.CLIENT)
public class BabyCarriedBlockLayer extends RenderLayer<BabyEnderman, BabyEndermanModel<BabyEnderman>> {
	private final BlockRenderDispatcher blockRenderer;

	public BabyCarriedBlockLayer(RenderLayerParent<BabyEnderman, BabyEndermanModel<BabyEnderman>> pRenderer, BlockRenderDispatcher pBlockRenderer) {
		super(pRenderer);
		this.blockRenderer = pBlockRenderer;
	}

	@Override
	public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, BabyEnderman pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		BlockState blockstate = pLivingEntity.getCarriedBlock();
		if (blockstate != null) {
			pPoseStack.pushPose();
			pPoseStack.translate(0.0F, 0.6875F, -0.75F);
			pPoseStack.mulPose(Axis.XP.rotationDegrees(20.0F));
			pPoseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
			float offsetY = pLivingEntity.isInSittingPose() ? 0.08F : 0;
			float offset = pLivingEntity.isInSittingPose() ? 0.38F : 0;
			pPoseStack.translate(0.25F + offset, 0.6F + offsetY, 0.25F - offset);
			float f = 0.5F;
			pPoseStack.scale(-0.5F, -0.5F, 0.5F);
			pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
			this.blockRenderer.renderSingleBlock(blockstate, pPoseStack, pBuffer, pPackedLight,
					OverlayTexture.NO_OVERLAY);
			pPoseStack.popPose();
		}
	}
}