package net.tinyfoes.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.tinyfoes.common.CommonTinyFoes;
import net.tinyfoes.common.registry.ModEffects;
import net.tinyfoes.common.util.ModUtil;
import net.tinyfoes.common.util.TinyFoesResLoc;

public class FabricTinyFoes implements ModInitializer {
	@Override
	public void onInitialize() {
		CommonTinyFoes.init();
		FabricItemGroupBuilder.create(new TinyFoesResLoc("tiny_tab")).appendItems(CommonTinyFoes::fillTabItems).icon(
				() -> ModUtil.TINY_TAB_ICON).build();
		FabricBrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(Items.POISONOUS_POTATO),
				ModEffects.BABYFICATION_POTION.get());
	}
}
