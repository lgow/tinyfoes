package net.tinyallies.client.renderer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.tinyallies.client.layer.GlowingEyesLayer;
import net.tinyallies.client.model.SpideyModel;
import net.tinyallies.entity.Spidey;

public class SpideyRenderer <T extends Spidey> extends MobRenderer<T, SpideyModel<T>> {
	private static final ResourceLocation SPIDER_LOCATION = new ResourceLocation("textures/entity/spider/spider.png");
	private static final ResourceLocation SPIDER_EYES_LOCATION = new ResourceLocation(
			"textures/entity/spider_eyes.png");

	public SpideyRenderer(EntityRendererProvider.Context p_174401_) {
		this(p_174401_, ModelLayers.SPIDER);
	}

	public SpideyRenderer(EntityRendererProvider.Context pContext, ModelLayerLocation pLayer) {
		super(pContext, new SpideyModel<>(pContext.bakeLayer(pLayer)), 0.4F);
		this.addLayer(new GlowingEyesLayer<>(this, SPIDER_EYES_LOCATION));
	}

	@Override
	public Vec3 getRenderOffset(T entity, float f) {
		return entity.isInSittingPose() ? new Vec3(0, -0.09, 0) : super.getRenderOffset(entity, f);
	}

	protected float getFlipDegrees(T pLivingEntity) {
		return 180.0F;
	}

	public ResourceLocation getTextureLocation(T pEntity) {
		return SPIDER_LOCATION;
	}
}