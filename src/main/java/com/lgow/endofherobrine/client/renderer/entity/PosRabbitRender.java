package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.client.layer.RabbitEyesLayer;
import com.lgow.endofherobrine.util.ModResourceLocation;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RabbitRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PosRabbitRender extends RabbitRenderer {
	public PosRabbitRender(EntityRendererProvider.Context context) {
		super(context);
		this.addLayer(new RabbitEyesLayer<>(this));
	}

	public ResourceLocation getTextureLocation(Rabbit pEntity) {
		String rabbitType;
		if (ChatFormatting.stripFormatting(pEntity.getName().getString()).equals("Toast")) { rabbitType = "toast"; }
		else if (pEntity.getVariant() == Rabbit.Variant.EVIL) { rabbitType = "caerbannog"; }
		else { rabbitType = pEntity.getVariant().getSerializedName(); }
		return new ModResourceLocation("textures/entity/rabbit/" + rabbitType + ".png");
	}
}
