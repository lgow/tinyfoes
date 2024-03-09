package net.tinyfoes.common;

import dev.architectury.registry.registries.Registries;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tinyfoes.common.items.ModItems;
import net.tinyfoes.common.registry.ModRegistries;
import net.tinyfoes.common.util.ModUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonTinyFoes {
	public static final String MODID = "tinyfoes";
	public static final Registries REGISTRIES = Registries.get(MODID);
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static void init() {
		ModRegistries.register();
	}

	public static void commonInit() {
		NonNullList<ItemStack> nonNullList = NonNullList.create();
		for (Item item : ModUtil.TAB_ITEM_LIST) {
			nonNullList.add(new ItemStack(item));
		}
		ModItems.TINY_TAB.fillItemList(nonNullList);
	}
}