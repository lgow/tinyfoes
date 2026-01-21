package github.lgow.tinyfoes.fabric;

import net.fabricmc.api.ClientModInitializer;
import github.lgow.tinyfoes.common.CommonClientTinyFoes;

public class FabricClientTinyFoes implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CommonClientTinyFoes.preClientInit();
	}
}