package com.lgow.endofherobrine.tileentities;

import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.registries.RegistryObject;

import static com.lgow.endofherobrine.registries.ModRegistries.MOD_PAINTINGS;
//fixme not registered
public class ModPaintingInit {

    public static RegistryObject<PaintingVariant> registerPainting(String name, int width, int height){
        return MOD_PAINTINGS.register(name, () -> new PaintingVariant(width, height));
    }

    public static final RegistryObject<PaintingVariant> ALTAR = registerPainting("altar", 32, 32);
    public static final RegistryObject<PaintingVariant> BLACK = registerPainting("blackstone", 16, 32);
    public static final RegistryObject<PaintingVariant> CRY = registerPainting("cry",64, 64);
    public static final RegistryObject<PaintingVariant> EMPTY = registerPainting("empty", 32, 16);
    public static final RegistryObject<PaintingVariant> FIGHT = registerPainting("fight", 64, 32);
    public static final RegistryObject<PaintingVariant> LURKER = registerPainting("lurker", 32, 16);
    public static final RegistryObject<PaintingVariant> NETHER = registerPainting("netherrack", 16, 32);

    public static void register() {
    }
}
