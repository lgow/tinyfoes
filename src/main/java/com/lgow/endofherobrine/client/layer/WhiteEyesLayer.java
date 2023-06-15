package com.lgow.endofherobrine.client.layer;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.config.ModConfigs;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WhiteEyesLayer <T extends PathfinderMob, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private final String resLoc;

	private final boolean alwaysRender;

	public WhiteEyesLayer(RenderLayerParent<T, M> rendererIn, String textureLoc) {
		this(rendererIn, textureLoc, false);
	}

	public WhiteEyesLayer(RenderLayerParent<T, M> rendererIn, String textureLoc, boolean alwaysRender) {
		super(rendererIn);
		this.resLoc = "textures/entity/layer/" + textureLoc;
		this.alwaysRender = alwaysRender;
	}

	public WhiteEyesLayer(RenderLayerParent<T, M> rendererIn) {
		this(rendererIn, "", false);
	}

	public String getEyeTexture(T entity) {
		return this.resLoc;
	}

	//returns if eye render type glows or not
	public RenderType getRenderType(T entity) {
		if (ModConfigs.EYE_GLOW.get()) {
			return RenderType.eyes(new ResourceLocation(Main.MOD_ID, getEyeTexture(entity)));
		}
		else {
			return RenderType.entityCutoutNoCull(new ResourceLocation(Main.MOD_ID, getEyeTexture(entity)));
		}
	}

	private boolean shouldRender(T entity) {
		return (entity.getLevel().getNearestPlayer(entity, 40D) != null || entity.isAggressive()) || this.alwaysRender;
	}

	public void render(PoseStack poseStack, MultiBufferSource source, int lightIn, T entity, float swing, float swingAmount, float parTicks, float age, float headYaw, float headPitch) {
		float f = 1.0F;
		VertexConsumer vertex = source.getBuffer(getRenderType(entity));
		if (shouldRender(entity)) {
			this.getParentModel().renderToBuffer(poseStack, vertex, lightIn, OverlayTexture.NO_OVERLAY, f, f, f, f);
		}
	}
}
