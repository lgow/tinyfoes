package com.lgow.endofherobrine.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

public class ModTab{
        public static CreativeModeTab defaultTab = CreativeModeTabs.getDefaultTab();
//        public static final CreativeModeTab TAB = new CreativeModeTab(CreativeModeTab.Row.TOP, CreativeModeTabs.allTabs().size() + 1,
//                CreativeModeTab.Type.CATEGORY, Component.translatable("itemGroup.endofherobrine"), new ItemStack(ItemInit.HEROBRINE_HEAD_ITEM.get()),
//                new CreativeModeTab.DisplayItemsGenerator() {
//                        @Override
//                        public void accept(FeatureFlagSet pEnabledFeatures, CreativeModeTab.Output output, boolean pDisplayOperatorCreativeTab) {
//                                output.accept(BlockInit.MOSSY_COBBLESTONE.get());
//                                output.accept(BlockInit.MOSSY_STONE_BRICKS.get());
//                                output.accept(BlockInit.GLOWING_OBSIDIAN.get());
//                                output.accept(BlockInit.GLOWSTONE.get());
//                                output.accept(ItemInit.CURSED_HEAD_ITEM.get());
//                                output.accept(BlockInit.NETHERRACK_TOTEM.get());
//                                output.accept(BlockInit.BLACKSTONE_TOTEM.get());
//                                output.accept(ItemInit.HEROBRINE_HEAD_ITEM.get());
//                                output.accept(ItemInit.BUILDER_EGG.get());
//                                output.accept(ItemInit.LURKER_EGG.get());
//                                output.accept(ItemInit.CHICKEN_EGG.get());
//                                output.accept(ItemInit.COW_EGG.get());
//                                output.accept(ItemInit.HUSK_EGG.get());
//                                output.accept(ItemInit.PIG_EGG.get());
//                                output.accept(ItemInit.PIGMAN_EGG.get());
//                                output.accept(ItemInit.SHEEP_EGG.get());
//                                output.accept(ItemInit.SILVERFISH_EGG.get());
//                                output.accept(ItemInit.SKELETON_EGG.get());
//                                output.accept(ItemInit.STRAY_EGG.get());
//                                output.accept(ItemInit.VILLAGER_EGG.get());
//                                output.accept(ItemInit.ZOMBIE_EGG.get());
//                                output.accept(ItemInit.ZOMBIE_VILLAGER_EGG.get());
//                    }
//            }, defaultTab.getBackgroundLocation(), defaultTab.hasSearchBar(), defaultTab.getSearchBarWidth(),
//                defaultTab.getTabsImage(), defaultTab.getLabelColor(), defaultTab.getSlotColor());

        public static void register() {}
}
