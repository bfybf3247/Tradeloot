package net.bfybf.tradeloot.fabric;

import net.bfybf.tradeloot.Tradeloot;
import net.fabricmc.api.ModInitializer;

import static net.bfybf.tradeloot.Tradeloot.register;

public final class TradelootFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Tradeloot.register();
        Tradeloot.init();
    }
}
