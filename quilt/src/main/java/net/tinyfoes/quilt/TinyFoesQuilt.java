package net.tinyfoes.quilt;

import net.tinyfoes.common.TinyFoesCommon;
import net.tinyfoes.common.client.TinyFoesClientCommon;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class TinyFoesQuilt implements ModInitializer {
	@Override
	public void onInitialize(ModContainer mod) {
		TinyFoesClientCommon.preClientInit();
		TinyFoesCommon.init();
	}
}
