package net.tinyapi.common.util;

import net.minecraft.resources.ResourceLocation;
import net.tinyapi.common.CommonTinyFoes;

public class TinyFoesResLoc extends ResourceLocation {
	public TinyFoesResLoc(String pPath) {
		super(CommonTinyFoes.MODID, pPath);
	}
}
