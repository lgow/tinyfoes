package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.client.layer.WhiteEyesLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PosSheepRender extends SheepRenderer {

    public PosSheepRender(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new WhiteEyesLayer<>(this,"sheep_eyes.png"));
    }

    @Override
    public ResourceLocation getTextureLocation(Sheep pEntity) {
        return new ResourceLocation(Main.MOD_ID,"textures/entity/sheep/sheep.png");
    }
}
