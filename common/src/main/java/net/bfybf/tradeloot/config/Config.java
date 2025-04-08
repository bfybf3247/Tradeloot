package net.bfybf.tradeloot.config;

import java.io.*;
import java.util.Properties;

import static net.bfybf.tradeloot.Tradeloot.configFile;

public class Config {

    public static boolean enableVillagerDrops;
    public static boolean requirePlayer;
    public static double dropsChance;
    public static double lootingBonus;
    public static int dropsBonus;
    public static int dropsNumber;
    public static boolean addInventory ;
    public static double invDropsChance;
    public static double PotatoChance;

    public static void loadConfig(File file) {
        Properties props = new Properties();
        try (InputStream is = new FileInputStream(configFile)) {
            props.load(is);

            enableVillagerDrops = Boolean.parseBoolean(props.getProperty("enableVillagerDrops", "true"));
            dropsChance = Double.parseDouble(props.getProperty("dropsChance", "0.25"));
            lootingBonus = Double.parseDouble(props.getProperty("lootingBonus", "0.15"));
            dropsBonus = Integer.parseInt(props.getProperty("dropsBonus", "1"));
            dropsNumber = Integer.parseInt(props.getProperty("dropsNumber", "2"));
            addInventory = Boolean.parseBoolean(props.getProperty("addInventory", "false"));
            invDropsChance = Double.parseDouble(props.getProperty("invDropsChance", "0.25"));
            PotatoChance = Double.parseDouble(props.getProperty("potatoChance", "0.02"));

        } catch (IOException e) {
            saveConfig();
        }
    }

    public static void saveConfig() {
        Properties props = new Properties();
        props.setProperty("enableVillagerDrops", String.valueOf(enableVillagerDrops));
        props.setProperty("requirePlayer", String.valueOf(requirePlayer));
        props.setProperty("dropsChance", String.valueOf(dropsChance));
        props.setProperty("lootingBonus", String.valueOf(lootingBonus));
        props.setProperty("dropsBonus", String.valueOf(dropsBonus));
        props.setProperty("dropsNumber", String.valueOf(dropsNumber));
        props.setProperty("addInventory", String.valueOf(addInventory));
        props.setProperty("invDropsChance", String.valueOf(invDropsChance));
        props.setProperty("potatoChance", String.valueOf(PotatoChance));

        try (OutputStream os = new FileOutputStream(configFile)) {
            props.store(os, "Tradeloot Config");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}