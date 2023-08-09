package com.lgow.endofherobrine.util;

import com.lgow.endofherobrine.Main;
import net.minecraft.resources.ResourceLocation;

public class ModResourceLocation extends ResourceLocation {
	public ModResourceLocation(String pPath) {
		super(Main.MOD_ID, pPath);
	}
}
