package com.lgow.endofherobrine.client.layer;

import net.minecraft.client.model.RabbitModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RabbitEyesLayer <T extends Rabbit, M extends RabbitModel<T>> extends WhiteEyesLayer<T, M>{

    public RabbitEyesLayer(RenderLayerParent<T,M> rendererIn) {
        super(rendererIn);
    }

    public String getEyeTexture(Rabbit rabbit) {
        String textureLoc = rabbit.getVariant() == Rabbit.Variant.EVIL ? "caerbannog_eyes.png" : "rabbit_eyes.png";
        return "textures/entity/layer/" + textureLoc;
    }
}
