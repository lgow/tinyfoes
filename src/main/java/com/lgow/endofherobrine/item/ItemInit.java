package com.lgow.endofherobrine.item;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.lgow.endofherobrine.block.BlockInit.*;
import static com.lgow.endofherobrine.entity.EntityInit.*;
import static com.lgow.endofherobrine.event.ModSoundEvents.ITS_HEROBRINE;
import static com.lgow.endofherobrine.event.ModSoundEvents.SEEN_HEROBRINE;
import static com.lgow.endofherobrine.registries.ModRegistries.MOD_ITEMS;

public class ItemInit {

    public static RegistryObject<ForgeSpawnEggItem> registerSpawnEgg(String name, Supplier<? extends EntityType<? extends Mob>> entityType, int color) {
        return MOD_ITEMS.register(name + "_spawn_egg", () -> new ForgeSpawnEggItem(entityType, color, 16777215,
                new Item.Properties()));
    }

    public static RegistryObject<ForgeSpawnEggItem> registerHerobrineSpawnEgg(String name, Supplier<? extends EntityType<? extends Mob>> entityType) {
        return registerSpawnEgg(name,entityType,44975);
    }

    public static RegistryObject<RecordItem> registerDisc(String name, int compValue, Supplier<SoundEvent> sound, int lenght) {
        return MOD_ITEMS.register("music_disc_" + name, () -> new RecordItem(compValue, sound,
                new Item.Properties().stacksTo(1).rarity(Rarity.RARE), lenght));
    }

    //HEADS
    public static final RegistryObject<StandingAndWallBlockItem> HEROBRINE_HEAD_ITEM = MOD_ITEMS.register("herobrine_head",
            () -> new StandingAndWallBlockItem(HEROBRINE_SKULL.get(), HEROBRINE_WALL_SKULL.get(),
                    new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant(), Direction.DOWN));

    public static final RegistryObject<StandingAndWallBlockItem> CURSED_HEAD_ITEM = MOD_ITEMS.register("cursed_head",
            () -> new StandingAndWallBlockItem(CURSED_SKULL.get(), CURSED_WALL_SKULL.get(),
                    new Item.Properties().stacksTo(1), Direction.DOWN));

    //DISCS
    public static final RegistryObject<RecordItem> MUSIC_DISC_ITEM_ITS_HEROBRINE =
            registerDisc("its_herobrine",14, ITS_HEROBRINE,1360);
    public static final RegistryObject<RecordItem> MUSIC_DISC_ITEM_HAVE_YOU_SEEN =
            registerDisc("seen_herobrine", 15, SEEN_HEROBRINE,2740);

    //SPAWN EGGS
    public static final RegistryObject<ForgeSpawnEggItem> BUILDER_EGG = registerHerobrineSpawnEgg("builder", BUILDER);
    public static final RegistryObject<ForgeSpawnEggItem> LURKER_EGG = registerHerobrineSpawnEgg("lurker", LURKER);
    public static final RegistryObject<ForgeSpawnEggItem> CHICKEN_EGG = registerSpawnEgg("chicken", POS_CHICKEN, 10489616);
    public static final RegistryObject<ForgeSpawnEggItem> COW_EGG = registerSpawnEgg("cow", POS_COW, 4470310);
    public static final RegistryObject<ForgeSpawnEggItem> PIG_EGG = registerSpawnEgg("pig", POS_PIG, 15771042);
    public static final RegistryObject<ForgeSpawnEggItem> PIGMAN_EGG = registerSpawnEgg("pigman", POS_PIGMAN, 15373203);
    public static final RegistryObject<ForgeSpawnEggItem> HUSK_EGG = registerSpawnEgg("husk", POS_HUSK, 12691306);
    public static final RegistryObject<ForgeSpawnEggItem> RABBIT_EGG = registerSpawnEgg("rabbit", POS_RABBIT, 10051392);
    public static final RegistryObject<ForgeSpawnEggItem> SHEEP_EGG = registerSpawnEgg("sheep", POS_SHEEP, 15198183);
    public static final RegistryObject<ForgeSpawnEggItem> SILVERFISH_EGG = registerSpawnEgg("silverfish", POS_SILVERFISH, 7237230);
    public static final RegistryObject<ForgeSpawnEggItem> SKELETON_EGG = registerSpawnEgg("skeleton", POS_SKELETON, 12698049);
    public static final RegistryObject<ForgeSpawnEggItem> STRAY_EGG = registerSpawnEgg("stray", POS_STRAY, 14543594);
    public static final RegistryObject<ForgeSpawnEggItem> VILLAGER_EGG = registerSpawnEgg("villager", POS_VILLAGER, 12422002);
    public static final RegistryObject<ForgeSpawnEggItem> ZOMBIE_EGG = registerSpawnEgg("zombie", POS_ZOMBIE, 3232308);
    public static final RegistryObject<ForgeSpawnEggItem> ZOMBIE_VILLAGER_EGG = registerSpawnEgg("zombie_villager", POS_ZOMBIE_VILLAGER, 7969893);

    public static void register(){}
}