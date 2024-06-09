package net.tinyapi.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.tinyapi.common.CommonClientTinyFoes;

public class FabricClientTinyFoes implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CommonClientTinyFoes.preClientInit();
	}
}