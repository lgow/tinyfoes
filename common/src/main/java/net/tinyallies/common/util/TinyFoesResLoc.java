package net.tinyallies.common.util;

import net.minecraft.resources.ResourceLocation;
import net.tinyallies.common.TinyFoesCommon;

public class TinyFoesResLoc extends ResourceLocation {
	public TinyFoesResLoc(String pPath) {
		super(TinyFoesCommon.MODID, pPath);
	}
}
