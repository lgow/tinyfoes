package com.lgow.endofherobrine.event;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.registries.ModRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;

public class ModSoundEvents {

    public static RegistryObject<SoundEvent> registerSoundEvent(String name){
        return ModRegistries.MOD_SOUND_EVENTS.register("music_disc." + name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Main.MOD_ID, name)));
    }

    public static final RegistryObject<SoundEvent> ITS_HEROBRINE = registerSoundEvent("its_herobrine");
    public static final RegistryObject<SoundEvent> SEEN_HEROBRINE = registerSoundEvent("seen_herobrine");

    public static void register(){  }
}