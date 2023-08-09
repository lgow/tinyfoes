package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.client.layer.WhiteEyesLayer;
import com.lgow.endofherobrine.util.ModResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HuskRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PosHuskRender extends HuskRenderer {
	public PosHuskRender(EntityRendererProvider.Context context) {
		super(context);
		this.addLayer(new WhiteEyesLayer<>(this, "biped_eyes.png"));
	}

	@Override
	public ResourceLocation getTextureLocation(Zombie pEntity) {
		return new ModResourceLocation("textures/entity/zombie/husk.png");
	}
}
