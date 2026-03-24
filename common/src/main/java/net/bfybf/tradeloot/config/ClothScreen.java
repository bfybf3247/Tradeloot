
package net.bfybf.tradeloot.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

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

        general.addEntry(entryBuilder.startIntField(Component.translatable("config.tradeloot.dropsNumber"), Config.dropsNumber)
                .setDefaultValue(2)
                .setSaveConsumer(value -> Config.dropsNumber = value)
                .setTooltip(Component.translatable("config.tradeloot.dropsNumber.tooltip"))
                .build());

        general.addEntry(entryBuilder.startIntField(Component.translatable("config.tradeloot.dropsBonus"), Config.dropsBonus)
                .setDefaultValue(1)
                .setSaveConsumer(value -> Config.dropsBonus = value)
                .setTooltip(Component.translatable("config.tradeloot.dropsBonus.tooltip"))
                .build());

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("config.tradeloot.looting_Bonus"), Config.lootingBonus)
                .setDefaultValue(0.15)
                .setSaveConsumer(value -> Config.lootingBonus = value)
                .setTooltip(Component.translatable("config.tradeloot.looting_Bonus.tooltip"))
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

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("config.tradeloot.PotatoChance"), Config.PotatoChance)
                .setDefaultValue(0.002)
                .setSaveConsumer(value -> Config.PotatoChance = value)
                .setTooltip(Component.translatable("config.tradeloot.PotatoChance.tooltip"))
                .build());

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("config.tradeloot.PenaltyChance"), Config.PenaltyChance)
                .setDefaultValue(0.10)
                .setSaveConsumer(value -> Config.PenaltyChance = value)
                .setTooltip(Component.translatable("config.tradeloot.PenaltyChance.tooltip"))
                .build());

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("config.tradeloot.BabyPenaltyMultiplier"), Config.BabyPenaltyMultiplier)
                .setDefaultValue(10.00)
                .setSaveConsumer(value -> Config.BabyPenaltyMultiplier = value)
                .setTooltip(Component.translatable("config.tradeloot.BabyPenaltyMultiplier.tooltip"))
                .build());

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("config.tradeloot.PunishLightingWeight"), Config.PunishLightingWeight)
                .setDefaultValue(0.33)
                .setSaveConsumer(value -> Config.PunishLightingWeight = value)
                .setTooltip(Component.translatable("config.tradeloot.PunishLightingWeight.tooltip"))
                .build());

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("config.tradeloot.PunishIronManWeight"), Config.PunishIronManWeight)
                .setDefaultValue(0.33)
                .setSaveConsumer(value -> Config.PunishIronManWeight = value)
                .setTooltip(Component.translatable("config.tradeloot.PunishIronManWeight.tooltip"))
                .build());

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("config.tradeloot.PunishJohnnyWeight"), Config.PunishJohnnyWeight)
                .setDefaultValue(0.33)
                .setSaveConsumer(value -> Config.PunishJohnnyWeight = value)
                .setTooltip(Component.translatable("config.tradeloot.PunishJohnnyWeight.tooltip"))
                .build());

        general.addEntry(entryBuilder.startStrList(Component.translatable("config.tradeloot.Potatoes"), Config.Potatoes)
                .setDefaultValue(Arrays.asList(
                        "minecraft:potato",
                        "minecraft:poisonous_potato",
                        "minecraft:pufferfish",
                        "minecraft:rotten_flesh",
                        "minecraft:spider_eye",
                        "minecraft:diorite",
                        "minecraft:lead",
                        "minecraft:book",
                        "minecraft:knowledge_book",
                        "minecraft:netherite_ingot"
                ))
                .setSaveConsumer(value -> Config.Potatoes = value)
                .setTooltip(Component.translatable("config.tradeloot.Potatoes.tooltip"))
                .build());

        general.addEntry(entryBuilder.startStrList(Component.translatable("config.tradeloot.Apples"), Config.Apples)
                .setDefaultValue(Arrays.asList(
                        "item.minecraft.apple",
                        "item.minecraft.golden_apple",
                        "item.minecraft.enchanted_golden_apple",
                        "item.minecraft.cooked_beef",
                        "item.minecraft.ender_eye",
                        "block.minecraft.diorite",
                        "entity.minecraft.wandering_trader",
                        "enchantment.minecraft.mending",
                        "lectern.take_book",
                        "item.minecraft.nether_brick"
                ))
                .setSaveConsumer(value -> Config.Apples = value)
                .setTooltip(Component.translatable("config.tradeloot.Apples.tooltip"))
                .build());

        return builder.build();

    }




}
