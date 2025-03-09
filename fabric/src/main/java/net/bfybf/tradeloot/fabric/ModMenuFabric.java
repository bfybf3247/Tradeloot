package net.bfybf.tradeloot.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.bfybf.tradeloot.config.ClothScreen;

public class ModMenuFabric implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ClothScreen::getConfigScreen;
    }


}
