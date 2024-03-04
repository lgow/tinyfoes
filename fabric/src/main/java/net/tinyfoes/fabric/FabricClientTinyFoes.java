package net.tinyfoes.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.tinyfoes.common.CommonClientTinyFoes;

public class FabricClientTinyFoes implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CommonClientTinyFoes.preClientInit();
	}
}