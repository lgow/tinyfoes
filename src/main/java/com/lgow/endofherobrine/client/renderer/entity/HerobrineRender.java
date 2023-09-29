package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.client.layer.WhiteEyesLayer;
import com.lgow.endofherobrine.config.ModConfigs;
import com.lgow.endofherobrine.entity.herobrine.AbstractHerobrine;
import com.lgow.endofherobrine.util.ModResourceLocation;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HerobrineRender extends LivingEntityRenderer<AbstractHerobrine, PlayerModel<AbstractHerobrine>> {
	private final RandomSource random = RandomSource.create();

	public HerobrineRender(EntityRendererProvider.Context context) {
		super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0);
		this.addLayer(new WhiteEyesLayer<>(this, "biped_eyes.png", true));
		this.addLayer(new HumanoidArmorLayer<>(this,
				new HumanoidArmorModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
				new HumanoidArmorModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractHerobrine herobrine) {
		return new ModResourceLocation("textures/entity/herobrine.png");
	}

	@Override
	protected boolean shouldShowName(AbstractHerobrine herobrine) {
		return (ModConfigs.shouldShowHerobrineNametag() && super.shouldShowName(herobrine);
	}

	@Override
	public Vec3 getRenderOffset(AbstractHerobrine herobrine, float pPartialTicks) {
		double shakeIntensity = herobrine.getMaxHealth() / herobrine.getHealth() * 0.008D;
		return herobrine.getHealth() < herobrine.getMaxHealth() / 2.5 ? new Vec3(
				this.random.nextGaussian() * shakeIntensity, this.random.nextGaussian() * shakeIntensity, this.random.nextGaussian() * shakeIntensity)
				: super.getRenderOffset(herobrine, pPartialTicks);
	}
}

