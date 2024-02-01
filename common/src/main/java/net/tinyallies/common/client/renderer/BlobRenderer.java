package net.tinyallies.common.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.tinyallies.common.client.TinyFoesClientCommon;
import net.tinyallies.common.client.model.BabyfierBlobModel;
import net.tinyallies.common.util.TinyFoesResLoc;

public class BlobRenderer extends EntityRenderer<ThrowableProjectile> {
	private final BabyfierBlobModel model;

	public BlobRenderer(EntityRendererProvider.Context pContext) {
		super(pContext);
		this.model = new BabyfierBlobModel(pContext.bakeLayer(TinyFoesClientCommon.BLOB));
	}

	@Override
	public void render(ThrowableProjectile pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
		pPoseStack.pushPose();
		pPoseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F - pEntity.getYRot()));
		pPoseStack.translate(0.0, -0.6, 0.0);
		pPoseStack.scale(0.5F, 0.5F, 0.5F);
		this.model.setupAnim(pEntity, 0, 0.0F, 0.0F, pEntity.getYRot(), pEntity.getXRot());
		VertexConsumer vertex = pBuffer.getBuffer(RenderType.eyes(this.getTextureLocation(pEntity)));
		this.model.renderToBuffer(pPoseStack, vertex, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		pPoseStack.popPose();
		super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(ThrowableProjectile entity) {
		return new TinyFoesResLoc("textures/projectiles/blob.png");
	}
}