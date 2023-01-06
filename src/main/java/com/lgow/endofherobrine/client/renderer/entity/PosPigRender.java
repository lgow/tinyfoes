package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.client.layer.WhiteEyesLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PosPigRender extends PigRenderer {

    private final RandomSource random = RandomSource.create();

    public PosPigRender(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new WhiteEyesLayer<>(this, "pig_eyes.png"));
    }

    public Vec3 getRenderOffset(Pig pig, float pPartialTicks) {
        return pig.getLevel().hasNearbyAlivePlayer(pig.getX(),pig.getY(),pig.getZ(),4) ?
                new Vec3(this.random.nextGaussian() * 0.005D, 0.0D, this.random.nextGaussian() * 0.005D)
                : super.getRenderOffset(pig, pPartialTicks);
    }

    @Override
    public ResourceLocation getTextureLocation(Pig pEntity) {
        return new ResourceLocation(Main.MOD_ID ,"textures/entity/pig/pig.png");
    }
}
