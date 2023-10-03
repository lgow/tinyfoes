package net.tinyallies.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;
import net.tinyallies.TinyAlliesCommon;
import net.tinyallies.client.TinyAlliesClientCommon;

@Mod.EventBusSubscriber(modid = TinyAlliesCommon.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeTinyAlliesClient {
	@SubscribeEvent
	public static void onInitializeClient(RegisterEvent event) {
		TinyAlliesClientCommon.preClientInit();
	}
}