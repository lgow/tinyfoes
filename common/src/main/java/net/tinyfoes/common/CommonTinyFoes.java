package net.tinyfoes.common;

import dev.architectury.platform.Mod;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.tinyfoes.common.items.ModItems;
import net.tinyfoes.common.registry.ModEffects;
import net.tinyfoes.common.registry.ModRegistries;
import net.tinyfoes.common.util.ModUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonTinyFoes {
	public static final String MODID = "tinyfoes";
	public static final RegistrarManager REGISTRIES = RegistrarManager.get(MODID);
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static void init() {
		ModRegistries.register();
	}

	public static void commonInit() {
		for (Item item : ModUtil.TAB_ITEM_LIST) {
			CreativeTabRegistry.append(ModItems.TINY_TAB, item);
		}
	}
}