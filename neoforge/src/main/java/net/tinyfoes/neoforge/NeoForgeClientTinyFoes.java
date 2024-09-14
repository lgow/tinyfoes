package net.tinyfoes.neoforge;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.tinyfoes.common.CommonClientTinyFoes;

public class NeoForgeClientTinyFoes {
	@SubscribeEvent
	public static void onInitializeClient(RegisterEvent event) {
		CommonClientTinyFoes.preClientInit();
	}
}