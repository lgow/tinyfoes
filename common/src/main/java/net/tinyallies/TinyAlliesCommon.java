package net.tinyallies;

import dev.architectury.registry.registries.RegistrarManager;
import net.tinyallies.registry.ModRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TinyAlliesCommon {
    public static final String MODID = "tinyallies";
    public static final RegistrarManager REGISTRIES = RegistrarManager.get(MODID);
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static void init() {
        ModRegistries.register();
    }

    public static void commonSetup(){
    }
}