package net.bfybf.tradeloot;

import dev.architectury.platform.Platform;
import net.bfybf.tradeloot.event.VillagerDeathEvent;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.io.File;

import static net.bfybf.tradeloot.config.Config.loadConfig;

public final class Tradeloot {
    public static final String MOD_ID = "tradeloot";
    public static File configFile;
    public static final TagKey<Item> NOTARDELOOT = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(MOD_ID, "prevent_tradeloot"));

    public static void init() {
        new VillagerDeathEvent();
    }

    public static void register(){
        configFile  = new File(Platform.getConfigFolder().toFile(), "tradeloot.properties");
        loadConfig(configFile);


    }
}
