package net.tinyallies.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tinyallies.TinyAlliesCommon;
import net.tinyallies.items.ModItems;
import net.tinyallies.util.TinyAlliesResLoc;

@Mod(TinyAlliesCommon.MODID)
public class ForgeTinyAllies {
	public ForgeTinyAllies() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		EventBuses.registerModEventBus(TinyAlliesCommon.MODID, modEventBus);
		TinyAlliesCommon.init();
	}

	public static final CreativeModeTab TINY_TAB = new CreativeModeTab(CreativeModeTab.getGroupCountSafe(),
			TinyAlliesCommon.MODID + ".tiny_tab") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.BABYFIER.get());
		}

		@Override
		public void fillItemList(NonNullList<ItemStack> nonNullList) {
			super.fillItemList(nonNullList);
			nonNullList.add(new ItemStack(ModItems.BABYFIER.get()));
			nonNullList.add(new ItemStack(Items.CREEPER_SPAWN_EGG));
			nonNullList.add(new ItemStack(Items.ENDERMAN_SPAWN_EGG));
			nonNullList.add(new ItemStack(Items.SKELETON_SPAWN_EGG));
			nonNullList.add(new ItemStack(Items.SPIDER_SPAWN_EGG));
			nonNullList.add(new ItemStack(Items.ZOMBIE_SPAWN_EGG));
			nonNullList.add(new ItemStack(Items.GUNPOWDER));
			nonNullList.add(new ItemStack(Items.CHORUS_FRUIT));
			nonNullList.add(new ItemStack(Items.BONE_MEAL));
			nonNullList.add(new ItemStack(Items.BEEF));
		}
	};
}
