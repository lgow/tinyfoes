package net.tinyfoes.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tinyfoes.common.TinyFoesCommon;
import net.tinyfoes.common.items.ModItems;
import net.tinyfoes.common.util.ModUtil;
import net.tinyfoes.forge.networking.ForgePacketHandler;

@Mod(TinyFoesCommon.MODID)
public class ForgeTinyFoes {
	public static final CreativeModeTab TINY_TAB = new CreativeModeTab(CreativeModeTab.getGroupCountSafe(),
			TinyFoesCommon.MODID + ".tiny_tab") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.TINY_TAB.get());
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
		ForgePacketHandler.register();
		MinecraftForge.EVENT_BUS.register(this);
	}
}
