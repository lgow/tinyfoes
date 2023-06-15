package com.lgow.endofherobrine.item;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.block.BlockInit;
import com.lgow.endofherobrine.enchantment.EnchantmentInit;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTab {
	public static CreativeModeTab TAB;

	@SubscribeEvent
	public static void registerModTab(CreativeModeTabEvent.Register event) {
		TAB = event.registerCreativeModeTab(new ResourceLocation(Main.MOD_ID, "endofherobrine_tab"),
				builder -> builder.icon(() -> new ItemStack(ItemInit.HEROBRINE_HEAD_ITEM.get()))
						.title(Component.translatable("itemGroup.endofherobrine")).build());
	}

	@SubscribeEvent
	public static void addItemsToTab(CreativeModeTabEvent.BuildContents event) {
		if (event.getTab().equals(ModTab.TAB)) {
			event.accept(BlockInit.GLOWING_OBSIDIAN.get());
			event.accept(BlockInit.NETHERRACK_TOTEM.get());
			event.accept(BlockInit.BLACKSTONE_TOTEM.get());
			event.accept(ItemInit.CURSED_HEAD_ITEM.get());
			event.accept(ItemInit.HEROBRINE_HEAD_ITEM.get());
			event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(EnchantmentInit.BLESSING.get(), 1)));
			event.accept(ItemInit.BUILDER_SPAWN_EGG.get());
			event.accept(ItemInit.LURKER_SPAWN_EGG.get());
			event.accept(ItemInit.NIGHTMARE_SPAWN_EGG.get());
			event.accept(ItemInit.DOPPLEGANGER_SPAWN_EGG.get());
			event.accept(ItemInit.CHICKEN_SPAWN_EGG.get());
			event.accept(ItemInit.COW_SPAWN_EGG.get());
			event.accept(ItemInit.HUSK_SPAWN_EGG.get());
			event.accept(ItemInit.PIG_SPAWN_EGG.get());
			event.accept(ItemInit.PIGMAN_SPAWN_EGG.get());
			event.accept(ItemInit.SHEEP_SPAWN_EGG.get());
			event.accept(ItemInit.SILVERFISH_SPAWN_EGG.get());
			event.accept(ItemInit.SKELETON_SPAWN_EGG.get());
			event.accept(ItemInit.STRAY_SPAWN_EGG.get());
			event.accept(ItemInit.VILLAGER_SPAWN_EGG.get());
			event.accept(ItemInit.ZOMBIE_SPAWN_EGG.get());
			event.accept(ItemInit.ZOMBIE_SPAWN_VILLAGER_EGG.get());
		}
	}

	public static void register() { }
}
