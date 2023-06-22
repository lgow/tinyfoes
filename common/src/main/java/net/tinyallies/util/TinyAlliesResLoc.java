package net.tinyallies.util;

import net.minecraft.resources.ResourceLocation;
import net.tinyallies.TinyAlliesCommon;

public class TinyAlliesResLoc extends ResourceLocation {
	public TinyAlliesResLoc(String pPath) {
		super(TinyAlliesCommon.MODID, pPath);
	}
}
