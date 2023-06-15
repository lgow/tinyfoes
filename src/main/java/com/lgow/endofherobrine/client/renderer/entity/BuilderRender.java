package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.client.layer.WhiteEyesLayer;
import com.lgow.endofherobrine.entity.herobrine.AbstractHerobrine;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BuilderRender extends HerobrineRender {
	public BuilderRender(EntityRendererProvider.Context context) {
		super(context);
		this.addLayer(new WhiteEyesLayer<>(this, "biped_eyes.png", true));
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractHerobrine herobrine) {
		return super.getTextureLocation(herobrine);
	}

	//makes builder invisible
	@Override
	public boolean shouldRender(AbstractHerobrine pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
		return false;
	}
}

