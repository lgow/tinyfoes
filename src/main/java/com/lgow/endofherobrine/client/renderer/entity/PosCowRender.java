package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.client.layer.WhiteEyesLayer;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PosCowRender extends CowRenderer {

    public PosCowRender(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new WhiteEyesLayer<>(this,"cow_eyes.png"));
    }

    @Override
    public ResourceLocation getTextureLocation(Cow pEntity) {
        return new ResourceLocation(Main.MOD_ID ,"textures/entity/cow/cow.png");
    }
}