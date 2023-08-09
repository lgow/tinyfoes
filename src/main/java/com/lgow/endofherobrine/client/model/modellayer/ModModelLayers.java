package com.lgow.endofherobrine.client.model.modellayer;

import com.lgow.endofherobrine.util.ModResourceLocation;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModModelLayers {
	public static final ModelLayerLocation VILLAGER, PIGMAN, DOPPELGANGER;

	static {
		VILLAGER = new ModelLayerLocation(new ModResourceLocation("villager"), "main");
		PIGMAN = new ModelLayerLocation(new ModResourceLocation("pigman"), "main");
		DOPPELGANGER = new ModelLayerLocation(new ModResourceLocation("doppleganger"), "main");

	}
}
