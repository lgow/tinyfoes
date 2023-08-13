package net.tinyallies.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import net.tinyallies.client.model.EnderBoyModel;
import net.tinyallies.entity.EnderBoy;

public class EnderBoyCarriedBlockLayer extends RenderLayer<EnderBoy, EnderBoyModel<EnderBoy>> {
	private final BlockRenderDispatcher blockRenderer;

	public EnderBoyCarriedBlockLayer(RenderLayerParent<EnderBoy, EnderBoyModel<EnderBoy>> pRenderer, BlockRenderDispatcher pBlockRenderer) {
		super(pRenderer);
		this.blockRenderer = pBlockRenderer;
	}

	@Override
	public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, EnderBoy pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		BlockState blockstate = pLivingEntity.getCarriedBlock();
		boolean sitting = pLivingEntity.isInSittingPose();
		if (blockstate != null) {
			pPoseStack.pushPose();
			pPoseStack.translate(0.0F, 0.6875F, -0.75F);
			pPoseStack.mulPose(Vector3f.XP.rotationDegrees(sitting ? 10.0F : 20F));
			pPoseStack.mulPose(Vector3f.YP.rotationDegrees(45.0F));
			float offsetY = sitting ? 0.65F : 0;
			float handOffset = sitting ? -0.0F : -0.15F;
			pPoseStack.translate(0.25F + handOffset, 0.6F - offsetY, 0.25F - handOffset);
			pPoseStack.scale(-0.5F, -0.5F, 0.5F);
			pPoseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
			this.blockRenderer.renderSingleBlock(blockstate, pPoseStack, pBuffer, pPackedLight,
					OverlayTexture.NO_OVERLAY);
			pPoseStack.popPose();
		}
	}
}