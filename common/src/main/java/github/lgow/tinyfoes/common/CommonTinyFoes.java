package github.lgow.tinyfoes.common;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import github.lgow.tinyfoes.common.items.ModItems;
import github.lgow.tinyfoes.common.registry.ModRegistries;
import github.lgow.tinyfoes.common.util.ModUtil;
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
		for (ItemStack item : ModUtil.TAB_ITEM_LIST) {
			CreativeTabRegistry.appendStack(ModItems.TINY_TAB, item);
		}
		for (Item item : ModUtil.TAB_EGG_LIST) {
			CreativeTabRegistry.append(ModItems.TINY_TAB, item);
		}
	}
}