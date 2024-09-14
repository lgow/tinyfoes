package net.tinyfoes.common.util;

import net.minecraft.resources.ResourceLocation;
import net.tinyfoes.common.CommonTinyFoes;

public class TinyFoesResLoc  {
	public ResourceLocation TinyFoesResLoc(String pPath) {
		return ResourceLocation.fromNamespaceAndPath(CommonTinyFoes.MODID, pPath);
	}
}
