package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.client.layer.WhiteEyesLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PosSkeletonRender extends SkeletonRenderer {

    public PosSkeletonRender(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new WhiteEyesLayer<>(this,"skeleton_eyes.png"));
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractSkeleton pEntity) {
        return new ResourceLocation(Main.MOD_ID,"textures/entity/skeleton/skeleton.png");
    }


}
