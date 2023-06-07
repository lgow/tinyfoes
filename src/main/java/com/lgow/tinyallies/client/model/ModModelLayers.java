package com.lgow.tinyallies.client.model;
import com.lgow.tinyallies.Main;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModModelLayers {
	public static final ModelLayerLocation CREEPER, SPIDER;

	static {
		CREEPER = new ModelLayerLocation(new ResourceLocation(Main.MODID, "creeper"), "main");
		SPIDER = new ModelLayerLocation(new ResourceLocation(Main.MODID, "spider"), "main");
//		PIGMAN = new ModelLayerLocation(new ResourceLocation(Main.MOD_ID, "pigman"), "main");
	}
}
