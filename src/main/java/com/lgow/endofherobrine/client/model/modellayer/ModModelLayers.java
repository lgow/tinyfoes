package com.lgow.endofherobrine.client.model.modellayer;

import com.lgow.endofherobrine.Main;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModModelLayers{
    public static final ModelLayerLocation POS_VILLAGER = new ModelLayerLocation(new ResourceLocation(Main.MOD_ID, "villager"), "main");
    public static final ModelLayerLocation PIGMAN = new ModelLayerLocation(new ResourceLocation(Main.MOD_ID, "pigman"), "main");
}
