package net.tinyfoes.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tinyfoes.common.CommonTinyFoes;
import net.tinyfoes.common.registry.ModEffects;
import net.tinyfoes.forge.recipe.ModBrewingRecipe;

@Mod(CommonTinyFoes.MODID)
public class ForgeTinyFoes {
	public ForgeTinyFoes() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		EventBuses.registerModEventBus(CommonTinyFoes.MODID, modEventBus);
		CommonTinyFoes.init();
		modEventBus.addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			CommonTinyFoes.commonInit();
			BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(Potions.AWKWARD, Items.POISONOUS_POTATO.asItem(),
					ModEffects.BABYFICATION_POTION.get()));
		});
	}
}
