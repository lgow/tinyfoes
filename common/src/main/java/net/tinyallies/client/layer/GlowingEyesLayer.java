package net.tinyallies.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.PathfinderMob;

//@OnlyIn(Dist.CLIENT)
public class GlowingEyesLayer <T extends PathfinderMob, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private final ResourceLocation resLoc;

	public GlowingEyesLayer(RenderLayerParent<T, M> rendererIn, ResourceLocation textureLoc) {
		super(rendererIn);
		this.resLoc = textureLoc;
	}

	public void render(PoseStack poseStack, MultiBufferSource source, int lightIn, T entity, float swing, float swingAmount, float parTicks, float age, float headYaw, float headPitch) {
		float f = 1.0F;
		VertexConsumer vertex = source.getBuffer(RenderType.eyes(this.resLoc));
			this.getParentModel().renderToBuffer(poseStack, vertex, lightIn, OverlayTexture.NO_OVERLAY, f, f, f, f);

	}
}
