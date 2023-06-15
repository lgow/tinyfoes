package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.client.layer.WhiteEyesLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PosZombieRender extends ZombieRenderer {
	public PosZombieRender(EntityRendererProvider.Context context) {
		super(context);
		this.addLayer(new WhiteEyesLayer<>(this, "biped_eyes.png"));
	}

	@Override
	public ResourceLocation getTextureLocation(Zombie pEntity) {
		return new ResourceLocation(Main.MOD_ID, "textures/entity/zombie/zombie.png");
	}
}

