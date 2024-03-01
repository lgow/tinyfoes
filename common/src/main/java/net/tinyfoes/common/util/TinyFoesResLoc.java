package net.tinyfoes.common.util;

import net.minecraft.resources.ResourceLocation;
import net.tinyfoes.common.TinyFoesCommon;

public class TinyFoesResLoc extends ResourceLocation {
	public TinyFoesResLoc(String pPath) {
		super(TinyFoesCommon.MODID, pPath);
	}
}
