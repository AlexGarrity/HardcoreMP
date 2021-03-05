package net.initialposition.hardcoremp.listeners;

import net.initialposition.hardcoremp.util.ConsoleLogger;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Timestamp;
import java.util.function.Consumer;

import static net.initialposition.hardcoremp.util.ConfigOperations.ConfigKeys.*;

public class PlayerDeathListener implements Listener {

    JavaPlugin plugin;

    public PlayerDeathListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Clears a player's inventory, but skips items with Curse of Binding
     * @param player The player to clear the inventory of
     */
    public void clearInventoryExcludingBindingCurse(Player player) {
        // Get the player inventory
        PlayerInventory inventory = player.getInventory();
        // Iterate over all items in the inventory
        for (int i = 0; i < inventory.getSize(); ++i) {
            // Get the current item stack
            ItemStack itemStack = inventory.getItem(i);
            // If the stack doesn't exist, continue
            if (itemStack == null) continue;
            // If the item doesn't have curse of binding, clear the stack
            if (!itemStack.containsEnchantment(Enchantment.BINDING_CURSE)) {
                inventory.clear(i);
            }
            else {
                if (this.plugin.getConfig().getString(String.valueOf(HANDLE_CURSE_OF_BINDING)).equals("disenchant")) {
                    itemStack.removeEnchantment(Enchantment.BINDING_CURSE);
                }
            }
        }

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

        // cancel if player is holding a totem of undying
        if (p.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING ||
                p.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
            return;
        }

        // cancel death since we want to implement our own functionality
        event.setCancelled(true);

        // Create a consumer (takes 1 parameter, returns nothing) to hold our clear function
        Consumer<Player> clearFunction;

        // If we're deleting Curse of Binding items, use the regular clear function
        if (this.plugin.getConfig().getString(String.valueOf(HANDLE_CURSE_OF_BINDING)).equals("default")) {
            clearFunction = (Player player) -> {
                player.getInventory().clear();
            };
        }
        // If we're not, use the specialised function
        else {
            clearFunction = this::clearInventoryExcludingBindingCurse;
        }

        // clear players inventory and xp if flags are set, then kill player
        if (!this.plugin.getConfig().getBoolean(String.valueOf(DYING_PLAYER_DROPS_INVENTORY))) {
            clearFunction.accept(p);
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

            // Clear the inventories using the selected function
            for (Player currentPlayer : this.plugin.getServer().getOnlinePlayers()) {
                // Make sure the player inventory isn't cleared twice
                if (!currentPlayer.equals(p))
                    clearFunction.accept(currentPlayer);
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
