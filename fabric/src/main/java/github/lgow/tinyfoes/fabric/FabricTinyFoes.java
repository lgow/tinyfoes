package github.lgow.tinyfoes.fabric;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fml.config.ModConfig;
import github.lgow.tinyfoes.common.CommonTinyFoes;
import github.lgow.tinyfoes.common.commands.ModCommads;
import github.lgow.tinyfoes.common.config.TinyFoesConfigs;
import github.lgow.tinyfoes.common.registry.ModEffects;

public class FabricTinyFoes implements ModInitializer {
	@Override
	public void onInitialize() {
		ForgeConfigRegistry.INSTANCE.register(CommonTinyFoes.MODID, ModConfig.Type.SERVER, TinyFoesConfigs.SERVER_SPEC,
				"tinyfoes-server.toml");
		ForgeConfigRegistry.INSTANCE.register(CommonTinyFoes.MODID, ModConfig.Type.CLIENT, TinyFoesConfigs.CLIENT_SPEC,
				"tinyfoes-client.toml");
		CommonTinyFoes.init();
		CommonTinyFoes.commonInit();
		FabricBrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(Items.POISONOUS_POTATO),
				ModEffects.BABYFICATION_POTION.get());
		CommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess, environment) -> ModCommads.register(dispatcher));
	}
}
