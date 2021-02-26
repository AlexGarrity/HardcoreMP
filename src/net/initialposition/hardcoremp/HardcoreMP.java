package net.initialposition.hardcoremp;

import net.initialposition.hardcoremp.listeners.PlayerDeathListener;
import net.initialposition.hardcoremp.listeners.PlayerLoginOutListener;
import net.initialposition.hardcoremp.util.ConfigOperations;
import net.initialposition.hardcoremp.util.ConsoleLogger;
import net.initialposition.hardcoremp.util.PluginInfo;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import static net.initialposition.hardcoremp.util.ConfigOperations.ConfigKeys.OFFLINE_PLAYERS;

public class HardcoreMP extends JavaPlugin {

    private PlayerLoginOutListener playerLoginOutListener;

    @Override
    public void onEnable() {

        ConsoleLogger.consoleLog("This server is running HardcoreMP v" + PluginInfo.VERSION + " by "
                + PluginInfo.AUTHOR);

        // handle config loading
        if (!ConfigOperations.hasConfig(this)) {
            ConsoleLogger.consoleLog("Initializing new config file. Please make sure to edit it to your needs.");
            ConfigOperations.initializeNewConfig(this);
        }

        // add on death listener
        PlayerDeathListener deathListener = new PlayerDeathListener(this);
        getServer().getPluginManager().registerEvents(deathListener, this);

        // add login/logout listener
        playerLoginOutListener = new PlayerLoginOutListener(this);
        if (this.getConfig().contains(String.valueOf(OFFLINE_PLAYERS))) {
            playerLoginOutListener.loadPlayerOfflineList();
        }
        getServer().getPluginManager().registerEvents(playerLoginOutListener, this);
    }

    @Override
    public void onDisable() {
        ConsoleLogger.consoleLog("Shutting down HardcoreMP.");

        // unregister all listeners
        HandlerList.unregisterAll(this);

        // save the list of offline players to disk
        playerLoginOutListener.savePlayerOfflineList();
    }
}
