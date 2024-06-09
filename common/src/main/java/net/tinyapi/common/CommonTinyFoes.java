package net.tinyapi.common;

import dev.architectury.registry.registries.Registries;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tinyapi.common.registry.ModRegistries;
import net.tinyapi.common.util.ModUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CommonTinyFoes {
	public static final String MODID = "tinyapi";
	public static final Registries REGISTRIES = Registries.get(MODID);
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static void init() {
		ModRegistries.register();
	}

	public static void commonInit() {
	}

	public static void fillTabItems(List<ItemStack> n) {
		NonNullList<ItemStack> nonNullList = NonNullList.create();
		nonNullList.addAll(ModUtil.TAB_ITEM_LIST);
		for (Item item : ModUtil.TAB_EGG_LIST) {
			nonNullList.add(new ItemStack(item));
		}
		n.addAll(nonNullList);
	}
}