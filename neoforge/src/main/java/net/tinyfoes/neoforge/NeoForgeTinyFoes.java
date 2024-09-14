package net.tinyfoes.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.tinyfoes.common.CommonTinyFoes;
import net.tinyfoes.common.commands.ModCommads;
import net.tinyfoes.common.config.TinyFoesConfigs;

@Mod(CommonTinyFoes.MODID)
public class NeoForgeTinyFoes {
	public NeoForgeTinyFoes(IEventBus eventBus,ModContainer container) {
		CommonTinyFoes.init();
		NeoForge.EVENT_BUS.register(this);
		container.registerConfig(ModConfig.Type.SERVER, TinyFoesConfigs.SERVER_SPEC);
		container.registerConfig(ModConfig.Type.CLIENT, TinyFoesConfigs.CLIENT_SPEC);
	}

	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event) {
		ModCommads.register(event.getDispatcher());
	}

	@SubscribeEvent
	private void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(CommonTinyFoes::commonInit);
	}
}
