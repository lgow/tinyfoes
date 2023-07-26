package net.tinyallies.client.renderer.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.tinyallies.client.TinyAlliesCommonClient;
import net.tinyallies.client.model.BabifyerBlobModel;
import net.tinyallies.entity.projectile.BabyfierBlob;
import net.tinyallies.util.TinyAlliesResLoc;

public class BlobRenderer extends EntityRenderer<BabyfierBlob> {
	private final BabifyerBlobModel model;

	public BlobRenderer(EntityRendererProvider.Context pContext) {
		super(pContext);
		this.model = new BabifyerBlobModel(pContext.bakeLayer(TinyAlliesCommonClient.BLOB));
	}

	@Override
	public void render(BabyfierBlob pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
		pPoseStack.pushPose();
		pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F - pEntity.getYRot()));
		pPoseStack.translate(0.0, -0.6, 0.0);
		pPoseStack.scale(0.5F, 0.5F, 0.5F);
		this.model.setupAnim(pEntity, 0, 0.0F, 0.0F, pEntity.getYRot(), pEntity.getXRot());
		VertexConsumer vertex = pBuffer.getBuffer(RenderType.eyes(this.getTextureLocation(pEntity)));
		this.model.renderToBuffer(pPoseStack, vertex, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		pPoseStack.popPose();
		super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(BabyfierBlob entity) {
		return new TinyAlliesResLoc("textures/projectiles/blob.png");
	}
}