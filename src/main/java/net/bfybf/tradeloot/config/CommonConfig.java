package net.bfybf.tradeloot.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class CommonConfig {

    private static ForgeConfigSpec.BooleanValue enable_villager_drops;

    private static ForgeConfigSpec.BooleanValue require_player;

    private static ForgeConfigSpec.DoubleValue drops_chance;

    private static ForgeConfigSpec.DoubleValue looting_bonus;

    private static ForgeConfigSpec.IntValue drops_number;

    private static ForgeConfigSpec.BooleanValue drop_inventory;

    private static ForgeConfigSpec.DoubleValue inv_drops_chance;

    public static boolean EnableVillagerDrops() {
        return enable_villager_drops.get();
    }

    public static boolean RequirePlayer(){
        return require_player.get();
    }

    public static double DropsChance() {
        return drops_chance.get();
    }

    public static double LootingBonus() {
        return looting_bonus.get();
    }

    public static int DropsNumber() {
        return drops_number.get();
    }

    public static boolean DropInventory(){
        return drop_inventory.get();
    }

    public static double InvDropsChance() {
        return inv_drops_chance.get();
    }

    public CommonConfig(ForgeConfigSpec.Builder builder){
        builder.comment("General settings of Tradeloot mod.");

        builder.push("Tradeloot");

        enable_villager_drops = builder.define("enable-villager-drops", true);

        require_player = builder.define("player-required", true);

        drops_chance = builder.comment("The basic chance should villager drop.").defineInRange("drops-chance",0.25,0,1);

        looting_bonus = builder.comment("The additional chance bonus that one level of looting adds.").defineInRange("looting-bonus",0.05,0,1);

        drops_number = builder.comment("The maximum number of drops of each villager level adds. Set '0' as infinite.").defineInRange("drops-number", 2, 0, 2147483647);

        drop_inventory = builder.comment("Should villager drop their inventory.").define("enable-inventory-drops", false);

        inv_drops_chance = builder.comment("The chance should villager drop their inventory.").defineInRange("inv-drops-chance",0.5,0,1);

        builder.pop();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
    }
}
