package net.tinyallies.client.renderer;

import net.tinyallies.client.layer.GlowingEyesLayer;
import net.tinyallies.client.model.BabySpiderModel;
import net.tinyallies.client.model.ModModelLayers;
import net.tinyallies.entity.BabySpider;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BabySpiderRender<T extends BabySpider> extends MobRenderer<T, BabySpiderModel<T>> {
	private static final ResourceLocation SPIDER_LOCATION = new ResourceLocation("textures/entity/spider/spider.png");
	private static final ResourceLocation SPIDER_EYES_LOCATION = new ResourceLocation("textures/entity/spider_eyes.png");

	public BabySpiderRender(EntityRendererProvider.Context p_174401_) {
		this(p_174401_, ModModelLayers.SPIDER);
	}

	public BabySpiderRender(EntityRendererProvider.Context pContext, ModelLayerLocation pLayer) {
		super(pContext, new BabySpiderModel<>(pContext.bakeLayer(pLayer)), 0.4F);
		this.addLayer(new GlowingEyesLayer<>(this, SPIDER_EYES_LOCATION));
	}

	protected float getFlipDegrees(T pLivingEntity) {
		return 180.0F;
	}

	public ResourceLocation getTextureLocation(T pEntity) {
		return SPIDER_LOCATION;
	}
}