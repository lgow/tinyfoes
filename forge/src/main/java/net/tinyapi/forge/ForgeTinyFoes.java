package net.tinyapi.forge;

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
import net.tinyapi.common.CommonTinyFoes;
import net.tinyapi.common.registry.ModEffects;
import net.tinyapi.common.util.ModUtil;
import net.tinyapi.forge.recipe.ModBrewingRecipe;
import org.jetbrains.annotations.NotNull;

@Mod(CommonTinyFoes.MODID)
public class ForgeTinyFoes {
	public static final CreativeModeTab TINY_TAB = new CreativeModeTab(CreativeModeTab.getGroupCountSafe(),
			CommonTinyFoes.MODID + ".tiny_tab") {
		@Override
		public @NotNull ItemStack makeIcon() {
			return ModUtil.TINY_TAB_ICON;
		}

		@Override
		public void fillItemList(NonNullList<ItemStack> nonNullList) {
			CommonTinyFoes.fillTabItems(nonNullList);
			super.fillItemList(nonNullList);
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
			CommonTinyFoes.commonInit();
			BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(Potions.AWKWARD, Items.POISONOUS_POTATO.asItem(),
					ModEffects.BABYFICATION_POTION.get()));
		});
	}
}
