package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.client.layer.WhiteEyesLayer;
import com.lgow.endofherobrine.config.ModConfigs;
import com.lgow.endofherobrine.entity.herobrine.AbstractHerobrine;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HerobrineRender extends LivingEntityRenderer<AbstractHerobrine, PlayerModel<AbstractHerobrine>> {
	public HerobrineRender(EntityRendererProvider.Context context) {
		super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0);
		this.addLayer(new WhiteEyesLayer<>(this, "biped_eyes.png", true));
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractHerobrine herobrine) {
		return new ResourceLocation(Main.MOD_ID, "textures/entity/herobrine.png");
	}

	@Override
	protected boolean shouldShowName(AbstractHerobrine herobrine) {
		return (ModConfigs.SHOW_NAMETAG.get() && herobrine.getDisplayName().getString().equals("Herobrine"))
				&& super.shouldShowName(herobrine);
	}
}

