package net.tinyfoes.fabric;

import fuzs.forgeconfigapiport.fabric.impl.core.NeoForgeConfigRegistryImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.fml.config.ModConfig;
import net.tinyfoes.common.CommonTinyFoes;
import net.tinyfoes.common.commands.ModCommads;
import net.tinyfoes.common.config.TinyFoesConfigs;
import net.tinyfoes.common.registry.ModEffects;

public class FabricTinyFoes implements ModInitializer {
	@Override
	public void onInitialize() {
		NeoForgeConfigRegistryImpl.INSTANCE.register(CommonTinyFoes.MODID, ModConfig.Type.SERVER,
				TinyFoesConfigs.SERVER_SPEC,
				"tinyfoes-server.toml");
		NeoForgeConfigRegistryImpl.INSTANCE.register(CommonTinyFoes.MODID, ModConfig.Type.CLIENT,
				TinyFoesConfigs.CLIENT_SPEC,
				"tinyfoes-client.toml");
		CommonTinyFoes.init();
		CommonTinyFoes.commonInit();
		FabricBrewingRecipeRegistryBuilder.BUILD.register((s) -> {
			s.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(Items.POISONOUS_POTATO),
					ModEffects.BABYFICATION_POTION);
		});
		CommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess, environment) -> ModCommads.register(dispatcher));
	}
}
