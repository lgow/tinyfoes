package net.tinyfoes.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.tinyfoes.common.CommonTinyFoes;
import net.tinyfoes.common.registry.ModEffects;

public class FabricTinyFoes implements ModInitializer {
	@Override
	public void onInitialize() {
		CommonTinyFoes.init();
		CommonTinyFoes.commonInit();
		FabricBrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(Items.POISONOUS_POTATO),
				ModEffects.BABYFICATION_POTION.get());
	}
}
