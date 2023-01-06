package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.client.layer.WhiteEyesLayer;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PosChickenRender extends ChickenRenderer {

    public PosChickenRender(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new WhiteEyesLayer<>(this,"chicken_eyes.png"));
    }

    @Override
    public ResourceLocation getTextureLocation(Chicken pEntity) {
        return new ResourceLocation(Main.MOD_ID ,"textures/entity/chicken.png");
    }
}
