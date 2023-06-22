package net.tinyallies.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.tinyallies.util.TinyAlliesResLoc;

public class ModModelLayers {
	public static final ModelLayerLocation CREEPER, SPIDER;

	static {
		CREEPER = new ModelLayerLocation(new TinyAlliesResLoc("creeper"), "main");
		SPIDER = new ModelLayerLocation(new TinyAlliesResLoc( "spider"), "main");
	}
}