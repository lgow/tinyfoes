package github.lgow.tinyfoes.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import github.lgow.tinyfoes.common.CommonTinyFoes;
import github.lgow.tinyfoes.common.commands.ModCommads;
import github.lgow.tinyfoes.common.config.TinyFoesConfigs;
import github.lgow.tinyfoes.common.registry.ModEffects;
import github.lgow.tinyfoes.forge.recipe.ModBrewingRecipe;

@Mod(CommonTinyFoes.MODID)
@Mod.EventBusSubscriber(modid = CommonTinyFoes.MODID)
public class ForgeTinyFoes {
	public ForgeTinyFoes() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		EventBuses.registerModEventBus(CommonTinyFoes.MODID, modEventBus);
		CommonTinyFoes.init();
		modEventBus.addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, TinyFoesConfigs.SERVER_SPEC, "tinyfoes-server.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, TinyFoesConfigs.CLIENT_SPEC, "tinyfoes-client.toml");
	}

	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event) {
		ModCommads.register(event.getDispatcher());
	}

	private void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			CommonTinyFoes.commonInit();
			BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(Potions.AWKWARD, Items.POISONOUS_POTATO.asItem(),
					ModEffects.BABYFICATION_POTION.get()));
		});
	}
}
