package net.tinyfoes.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tinyfoes.common.CommonTinyFoes;
import net.tinyfoes.common.items.ModItems;
import net.tinyfoes.common.registry.ModEffects;
import net.tinyfoes.common.util.ModUtil;

@Mod(CommonTinyFoes.MODID)
public class ForgeTinyFoes {
	public static final CreativeModeTab TINY_TAB = new CreativeModeTab(CreativeModeTab.getGroupCountSafe(),
			CommonTinyFoes.MODID + ".tiny_tab") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.TINY_TAB.get());
		}

		@Override
		public void fillItemList(NonNullList<ItemStack> nonNullList) {
			super.fillItemList(nonNullList);
			nonNullList.addAll(ModUtil.TAB_ITEM_LIST);
		}
	};

	public ForgeTinyFoes() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		EventBuses.registerModEventBus(CommonTinyFoes.MODID, modEventBus);
		CommonTinyFoes.init();
		modEventBus.addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, Items.POISONOUS_POTATO.asItem(),
					ModEffects.BABYFICATION_POTION.get()));
		});
	}
}
