package net.initialposition.hardcoremp.util;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigOperations {

    public enum ConfigKeys {
        DELETE_INVENTORIES_ON_DEATH,
        ONE_HIT_KILL,
        DYING_PLAYER_DROPS_INVENTORY,
        DYING_PLAYER_DROPS_EXP,
        LAST_DEATH_TIME,
        LAST_DEATH_NAME,
        OFFLINE_PLAYERS,
        HANDLE_CURSE_OF_BINDING,
        CLEAR_ENDER_CHESTS,
        RANDOM_CURSE_OF_VANISHING
    }

    public static void initializeNewConfig(JavaPlugin plugin) {

        // set default config values
        plugin.getConfig().set(String.valueOf(ConfigKeys.DELETE_INVENTORIES_ON_DEATH), true);
        plugin.getConfig().set(String.valueOf(ConfigKeys.ONE_HIT_KILL), false);
        plugin.getConfig().set(String.valueOf(ConfigKeys.DYING_PLAYER_DROPS_INVENTORY), false);
        plugin.getConfig().set(String.valueOf(ConfigKeys.DYING_PLAYER_DROPS_EXP), false);
        plugin.getConfig().set(String.valueOf(ConfigKeys.LAST_DEATH_TIME), -1);
        plugin.getConfig().set(String.valueOf(ConfigKeys.LAST_DEATH_NAME), "missingno.");

        plugin.getConfig().set(String.valueOf(ConfigKeys.HANDLE_CURSE_OF_BINDING), "delete");
        plugin.getConfig().set(String.valueOf(ConfigKeys.CLEAR_ENDER_CHESTS), "keep");
        plugin.getConfig().set(String.valueOf(ConfigKeys.RANDOM_CURSE_OF_VANISHING), 0);

        // save config
        plugin.saveConfig();
    }

    public static boolean hasConfig(JavaPlugin plugin) {
        return plugin.getConfig().contains(String.valueOf(ConfigKeys.DELETE_INVENTORIES_ON_DEATH));
    }
}
