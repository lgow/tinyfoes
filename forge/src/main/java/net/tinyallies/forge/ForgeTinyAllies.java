package net.tinyallies.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tinyallies.TinyAlliesCommon;

@Mod(TinyAlliesCommon.MODID)
public class ForgeTinyAllies {
	public ForgeTinyAllies() {
		// Submit our event bus to let architectury register our content on the right time
		EventBuses.registerModEventBus(TinyAlliesCommon.MODID, FMLJavaModLoadingContext.get().getModEventBus());
		TinyAlliesCommon.init();
	}
}
