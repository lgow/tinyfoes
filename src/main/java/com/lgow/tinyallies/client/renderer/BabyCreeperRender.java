package com.lgow.tinyallies.client.renderer;

import com.lgow.tinyallies.client.layer.BabyCreeperPowerLayer;
import com.lgow.tinyallies.client.model.BabyCreeperModel;
import com.lgow.tinyallies.entity.BabyCreeper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BabyCreeperRender extends MobRenderer<BabyCreeper, BabyCreeperModel> {
	private static final ResourceLocation CREEPER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper.png");

	public BabyCreeperRender(EntityRendererProvider.Context p_173958_) {
		super(p_173958_, new BabyCreeperModel(p_173958_.bakeLayer(ModelLayers.CREEPER)), 0.25F);
		this.addLayer(new BabyCreeperPowerLayer(this, p_173958_.getModelSet()));
	}

	protected void scale(BabyCreeper pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
		float f = pLivingEntity.getSwelling(pPartialTickTime);
		float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
		f = Mth.clamp(f, 0.0F, 1.0F);
		f *= f;
		f *= f;
		float f2 = (1.0F + f * 0.4F) * f1;
		float f3 = (1.0F + f * 0.1F) / f1;
		pMatrixStack.scale(f2, f3, f2);
	}

	protected float getWhiteOverlayProgress(BabyCreeper pLivingEntity, float pPartialTicks) {
		float f = pLivingEntity.getSwelling(pPartialTicks);
		return (int) (f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
	}

	public ResourceLocation getTextureLocation(BabyCreeper pEntity) {
		return CREEPER_LOCATION;
	}
}