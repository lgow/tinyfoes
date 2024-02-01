package net.tinyallies.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;
import net.tinyallies.common.TinyFoesCommon;
import net.tinyallies.common.client.TinyFoesClientCommon;

@Mod.EventBusSubscriber(modid = TinyFoesCommon.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeTinyFoesClient {
	@SubscribeEvent
	public static void onInitializeClient(RegisterEvent event) {
		TinyFoesClientCommon.preClientInit();
	}
}