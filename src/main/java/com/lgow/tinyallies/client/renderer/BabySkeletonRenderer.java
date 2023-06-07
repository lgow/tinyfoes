package com.lgow.tinyallies.client.renderer;

import com.lgow.tinyallies.client.layer.BabyArmorLayer;
import com.lgow.tinyallies.client.layer.BabyHeldItemLayer;
import com.lgow.tinyallies.client.model.BabySkeletonModel;
import com.lgow.tinyallies.entity.BabySkeleton;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;

public class BabySkeletonRenderer extends HumanoidMobRenderer<BabySkeleton, SkeletonModel<BabySkeleton>> {
	public BabySkeletonRenderer(EntityRendererProvider.Context p_174380_) {
		this(p_174380_, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
	}

	public BabySkeletonRenderer(EntityRendererProvider.Context pContext, ModelLayerLocation p_174383_, ModelLayerLocation pInnerModelLayer, ModelLayerLocation pOuterModelLayer){
		super(pContext, new BabySkeletonModel(pContext.bakeLayer(p_174383_)), 0.25F);
		this.layers.clear();
		this.addLayer(new BabyArmorLayer<>(this, new BabySkeletonModel(pContext.bakeLayer(pInnerModelLayer)), new BabySkeletonModel(pContext.bakeLayer(pOuterModelLayer)), pContext.getModelManager(), 0.34F));
		this.addLayer(new CustomHeadLayer<>(this, pContext.getModelSet(), 1.0F, 1.0F, 1.0F, pContext.getItemInHandRenderer()));
//		this.addLayer(new ElytraLayer<>(this, pContext.getModelSet()));
		this.addLayer(new BabyHeldItemLayer<>(this, pContext.getItemInHandRenderer()));
	}
	public ResourceLocation getTextureLocation(BabySkeleton pEntity) {
		return new ResourceLocation("textures/entity/skeleton/skeleton.png");
	}
}
	