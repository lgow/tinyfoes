package github.lgow.tinyfoes.common.util;

import net.minecraft.resources.ResourceLocation;
import github.lgow.tinyfoes.common.CommonTinyFoes;

public class TinyFoesResLoc extends ResourceLocation {
	public TinyFoesResLoc(String pPath) {
		super(CommonTinyFoes.MODID, pPath);
	}
}
