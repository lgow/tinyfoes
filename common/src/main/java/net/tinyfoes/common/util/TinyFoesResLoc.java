package net.tinyfoes.common.util;

import net.minecraft.resources.ResourceLocation;
import net.tinyfoes.common.CommonTinyFoes;

public class TinyFoesResLoc extends ResourceLocation {
	public TinyFoesResLoc(String pPath) {
		super(CommonTinyFoes.MODID, pPath);
	}
}
