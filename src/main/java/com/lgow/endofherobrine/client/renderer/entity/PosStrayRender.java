package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.client.layer.WhiteEyesLayer;
import com.lgow.endofherobrine.util.ModResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.StrayRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PosStrayRender extends StrayRenderer {
	public PosStrayRender(EntityRendererProvider.Context context) {
		super(context);
		this.addLayer(new WhiteEyesLayer<>(this, "skeleton_eyes.png"));
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractSkeleton pEntity) {
		return new ModResourceLocation("textures/entity/skeleton/stray.png");
	}
}
