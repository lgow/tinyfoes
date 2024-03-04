package net.tinyfoes.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;
import net.tinyfoes.common.CommonClientTinyFoes;
import net.tinyfoes.common.CommonTinyFoes;

@Mod.EventBusSubscriber(modid = CommonTinyFoes.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeClientTinyFoes {
	@SubscribeEvent
	public static void onInitializeClient(RegisterEvent event) {
		CommonClientTinyFoes.preClientInit();
	}
}