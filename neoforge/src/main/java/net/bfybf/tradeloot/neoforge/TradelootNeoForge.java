package net.bfybf.tradeloot.neoforge;

import net.bfybf.tradeloot.Tradeloot;
import net.bfybf.tradeloot.config.ClothScreen;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(Tradeloot.MOD_ID)
public final class TradelootNeoForge {
    public TradelootNeoForge() {
        Tradeloot.register();
        Tradeloot.init();
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, parent) -> ClothScreen.getConfigScreen(parent));

    }
}
