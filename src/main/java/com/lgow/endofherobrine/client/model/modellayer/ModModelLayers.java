package com.lgow.endofherobrine.client.model.modellayer;

import com.lgow.endofherobrine.Main;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModModelLayers {
	public static final ModelLayerLocation VILLAGER, PIGMAN, DOPPELGANGER;

	static {
		VILLAGER = new ModelLayerLocation(new ResourceLocation(Main.MOD_ID, "villager"), "main");
		PIGMAN = new ModelLayerLocation(new ResourceLocation(Main.MOD_ID, "pigman"), "main");
		DOPPELGANGER = new ModelLayerLocation(new ResourceLocation(Main.MOD_ID, "doppleganger"), "main");

	}
}
