package de.initialposition.hardcoremp.listeners;

import de.initialposition.hardcoremp.util.ConsoleLogger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Timestamp;

import static de.initialposition.hardcoremp.util.ConfigOperations.ConfigKeys.*;

public class PlayerDeathListener implements Listener {

    JavaPlugin plugin;

    public PlayerDeathListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handleDeath(EntityDamageEvent event) {

        // cancel if the damaged entity is not a player
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        // we can now safely get the damaged player instance
        Player p = (Player) event.getEntity();

        // check if damage was lethal (we have to check the entity directly) and cancel if it was, otherwise return
        if (((Player) event.getEntity()).getHealth() - event.getFinalDamage() > 0) {
            if (!this.plugin.getConfig().getBoolean(String.valueOf(ONE_HIT_KILL))) {
                return;
            }
        }

        // cancel death since we want to implement our own functionality
        event.setCancelled(true);

        // clear players inventory and xp if flags are set, then kill player
        if (!this.plugin.getConfig().getBoolean(String.valueOf(DYING_PLAYER_DROPS_INVENTORY))) {
            p.getInventory().clear();
        }

        if (!this.plugin.getConfig().getBoolean(String.valueOf(DYING_PLAYER_DROPS_EXP))) {
            p.setLevel(0);
            p.setExp(0);
        }

        p.setHealth(0);

        // if deleting inventories is enabled, do that
        if (this.plugin
                .getConfig()
                .getBoolean(String.valueOf(DELETE_INVENTORIES_ON_DEATH))) {
            ConsoleLogger.consoleLog("Resetting player inventories...");

            // delete inventories of online players
            for (Player currentPlayer : this.plugin.getServer().getOnlinePlayers()) {
                currentPlayer.getInventory().clear();
            }

            // set last death name and timestamp
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            this.plugin.getConfig().set(String.valueOf(LAST_DEATH_TIME), timestamp.getTime());

            this.plugin.getConfig().set(String.valueOf(LAST_DEATH_NAME), p.getDisplayName());
        }

        // save new data in the config file
        this.plugin.saveConfig();
    }
}
