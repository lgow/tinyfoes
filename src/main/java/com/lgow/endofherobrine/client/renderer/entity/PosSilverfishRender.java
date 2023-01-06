package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.client.layer.WhiteEyesLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SilverfishRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PosSilverfishRender extends SilverfishRenderer {

    public PosSilverfishRender(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new WhiteEyesLayer<>(this,"silverfish_eyes.png"));
    }

    @Override
    public ResourceLocation getTextureLocation(Silverfish pEntity) {
        return new ResourceLocation(Main.MOD_ID,"textures/entity/silverfish.png");
    }
}