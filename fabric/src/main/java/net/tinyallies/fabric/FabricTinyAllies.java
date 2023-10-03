package net.tinyallies.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tinyallies.TinyAlliesCommon;
import net.tinyallies.items.ModItems;
import net.tinyallies.util.TinyAlliesResLoc;

public class FabricTinyAllies implements ModInitializer {
	NonNullList<ItemStack> nonNullList = NonNullList.of(ItemStack.EMPTY,new ItemStack(ModItems.BABYFIER.get()),
			new ItemStack(Items.CREEPER_SPAWN_EGG), new ItemStack(Items.ENDERMAN_SPAWN_EGG),
			new ItemStack(Items.SKELETON_SPAWN_EGG), new ItemStack(Items.SPIDER_SPAWN_EGG),
			new ItemStack(Items.ZOMBIE_SPAWN_EGG), new ItemStack(Items.GUNPOWDER), new ItemStack(Items.CHORUS_FRUIT),
			new ItemStack(Items.BONE_MEAL), new ItemStack(Items.BEEF));

	@Override
	public void onInitialize() {
		TinyAlliesCommon.init();
		FabricItemGroupBuilder.create(new TinyAlliesResLoc("tiny_tab")).appendItems((n) -> n.addAll(nonNullList)).icon(() -> new ItemStack(ModItems.BABYFIER.get())).build();
	}
}
