package net.bfybf.tradeloot.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigManager {
    public static CommonConfig COMMON_CONFIG;

    public static void registerConfigs() {
        ForgeConfigSpec.Builder common = new ForgeConfigSpec.Builder();
        COMMON_CONFIG = new CommonConfig(common);
    }



}
