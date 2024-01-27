package net.tinyallies.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tinyallies.TinyFoesCommon;
import net.tinyallies.items.ModItems;
import net.tinyallies.util.ModUtil;

@Mod(TinyFoesCommon.MODID)
public class ForgeTinyFoes {
	public static final CreativeModeTab TINY_TAB = new CreativeModeTab(CreativeModeTab.getGroupCountSafe(),
			TinyFoesCommon.MODID + ".tiny_tab") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.BABYFIER.get());
		}

		@Override
		public void fillItemList(NonNullList<ItemStack> nonNullList) {
			super.fillItemList(nonNullList);
			for (Item item : ModUtil.TAB_ITEM_LIST) {
				nonNullList.add(new ItemStack(item));
			}
		}
	};

	public ForgeTinyFoes() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		EventBuses.registerModEventBus(TinyFoesCommon.MODID, modEventBus);
		TinyFoesCommon.init();
	}
}
