package net.tinyallies.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.tinyallies.TinyFoesCommon;
import net.tinyallies.util.ModUtil;
import net.tinyallies.util.TinyFoesResLoc;

import static net.tinyallies.util.ModUtil.TAB_ICON;

public class FabricTinyFoes implements ModInitializer {
	@Override
	public void onInitialize() {
		TinyFoesCommon.init();
		FabricItemGroupBuilder.create(new TinyFoesResLoc("tiny_tab")).appendItems((n) -> {
			for (ItemLike item : ModUtil.TAB_ITEM_LIST) {
				n.add(new ItemStack(item));
			}
		}).icon(
				() -> TAB_ICON).build();
	}
}
