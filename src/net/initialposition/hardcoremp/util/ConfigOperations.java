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
        HANDLE_CURSE_OF_BINDING
    }

    public static void initializeNewConfig(JavaPlugin plugin) {

        // set default config values
        plugin.getConfig().set(String.valueOf(ConfigKeys.DELETE_INVENTORIES_ON_DEATH), true);
        plugin.getConfig().set(String.valueOf(ConfigKeys.HANDLE_CURSE_OF_BINDING), "default");
        plugin.getConfig().set(String.valueOf(ConfigKeys.ONE_HIT_KILL), false);
        plugin.getConfig().set(String.valueOf(ConfigKeys.DYING_PLAYER_DROPS_INVENTORY), false);
        plugin.getConfig().set(String.valueOf(ConfigKeys.DYING_PLAYER_DROPS_EXP), false);
        plugin.getConfig().set(String.valueOf(ConfigKeys.LAST_DEATH_TIME), -1);
        plugin.getConfig().set(String.valueOf(ConfigKeys.LAST_DEATH_NAME), "missingno.");

        // save config
        plugin.saveConfig();
    }

    public static boolean hasConfig(JavaPlugin plugin) {
        return plugin.getConfig().contains(String.valueOf(ConfigKeys.DELETE_INVENTORIES_ON_DEATH));
    }
}
