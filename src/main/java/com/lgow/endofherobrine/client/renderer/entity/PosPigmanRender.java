package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.client.layer.WhiteEyesLayer;
import com.lgow.endofherobrine.client.model.PosPigmanModel;
import com.lgow.endofherobrine.client.model.modellayer.ModModelLayers;
import com.lgow.endofherobrine.entity.possessed.PosPigman;
import com.lgow.endofherobrine.util.ModResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PosPigmanRender extends HumanoidMobRenderer<PosPigman, PosPigmanModel<PosPigman>> {
	public PosPigmanRender(EntityRendererProvider.Context context) {
		super(context, new PosPigmanModel<>(context.bakeLayer(ModModelLayers.PIGMAN)), 0.5f);
		this.addLayer(new WhiteEyesLayer<>(this, "pigman_eyes.png"));
	}

	protected void scale(PosPigman pigman, PoseStack poseStack, float partTick) {
		float f = 1f;
		poseStack.scale(f, f, f);
		super.scale(pigman, poseStack, partTick);
	}

	@Override
	public ResourceLocation getTextureLocation(PosPigman p_113771_) {
		return new ModResourceLocation("textures/entity/pigman.png");
	}
}
