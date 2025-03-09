package net.bfybf.tradeloot.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClothScreen {


    public static Screen getConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.tradeloot.title"));

        builder.setSavingRunnable(Config::saveConfig);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("config.tradeloot.general"));

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.tradeloot.enable_villager_drops"), Config.enableVillagerDrops)
                .setDefaultValue(true)
                .setSaveConsumer(value -> Config.enableVillagerDrops = value)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.tradeloot.require_player"), Config.requirePlayer)
                .setDefaultValue(true)
                .setSaveConsumer(value -> Config.requirePlayer = value)
                .setTooltip(Component.translatable("config.tradeloot.require_player.tooltip"))
                .build());

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("config.tradeloot.drops_chance"), Config.dropsChance)
                .setDefaultValue(0.25)
                .setSaveConsumer(value -> Config.dropsChance = value)
                .setTooltip(Component.translatable("config.tradeloot.drops_chance.tooltip"))
                .build());

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("config.tradeloot.looting_Bonus"), Config.lootingBonus)
                .setDefaultValue(0.15)
                .setSaveConsumer(value -> Config.lootingBonus = value)
                .setTooltip(Component.translatable("config.tradeloot.looting_Bonus.tooltip"))
                .build());

        general.addEntry(entryBuilder.startIntField(Component.translatable("config.tradeloot.dropsNumber"), Config.dropsNumber)
                .setDefaultValue(2)
                .setSaveConsumer(value -> Config.dropsNumber = value)
                .setTooltip(Component.translatable("config.tradeloot.dropsNumber.tooltip"))
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.tradeloot.add_Inventory"), Config.addInventory)
                .setDefaultValue(false)
                .setSaveConsumer(value -> Config.addInventory = value)
                .setTooltip(Component.translatable("config.tradeloot.add_Inventory.tooltip"))
                .build());

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("config.tradeloot.invDrops_Chance"), Config.invDropsChance)
                .setDefaultValue(0.25)
                .setSaveConsumer(value -> Config.invDropsChance = value)
                .setTooltip(Component.translatable("config.tradeloot.invDrops_Chance.tooltip"))
                .build());

        return builder.build();
    }




}
