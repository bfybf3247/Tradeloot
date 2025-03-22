package net.bfybf.tradeloot.forge;

import net.bfybf.tradeloot.Tradeloot;
import dev.architectury.platform.forge.EventBuses;
import net.bfybf.tradeloot.config.ClothScreen;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Tradeloot.MOD_ID)
public final class TradelootForge {
    public TradelootForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(Tradeloot.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Tradeloot.register();
        Tradeloot.init();
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory((client, parent) -> {
            return ClothScreen.getConfigScreen(parent);
        }));
    }

}
