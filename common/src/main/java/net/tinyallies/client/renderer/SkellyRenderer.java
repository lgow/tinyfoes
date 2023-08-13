package net.tinyallies.client.renderer;

import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.tinyallies.client.layer.BabyHeldItemLayer;
import net.tinyallies.client.model.SkellyModel;
import net.tinyallies.entity.Skelly;

public class SkellyRenderer extends HumanoidMobRenderer<Skelly, SkeletonModel<Skelly>> {
	public SkellyRenderer(EntityRendererProvider.Context p_174380_) {
		this(p_174380_, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
	}

	public SkellyRenderer(EntityRendererProvider.Context pContext, ModelLayerLocation p_174383_, ModelLayerLocation pInnerModelLayer, ModelLayerLocation pOuterModelLayer) {
		super(pContext, new SkellyModel(pContext.bakeLayer(p_174383_)), 0.25F);
		this.layers.clear();
		this.addLayer(new HumanoidArmorLayer<>(this, new SkellyModel(pContext.bakeLayer(pInnerModelLayer)),
				new SkellyModel(pContext.bakeLayer(pOuterModelLayer))));
		this.addLayer(new CustomHeadLayer<>(this, pContext.getModelSet(), 1.0F, 1.0F, 1.0F,
				pContext.getItemInHandRenderer()));
		//		this.addLayer(new ElytraLayer<>(this, pContext.getModelSet()));
		this.addLayer(new BabyHeldItemLayer<>(this, pContext.getItemInHandRenderer()));
	}

	@Override
	public Vec3 getRenderOffset(Skelly entity, float f) {
		return entity.isInSittingPose() ? new Vec3(0, -0.343, 0) : super.getRenderOffset(entity, f);
	}

	public ResourceLocation getTextureLocation(Skelly pEntity) {
		return new ResourceLocation("textures/entity/skeleton/skeleton.png");
	}
}
	