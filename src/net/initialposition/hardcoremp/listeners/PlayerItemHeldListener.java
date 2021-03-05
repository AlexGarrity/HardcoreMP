package net.initialposition.hardcoremp.listeners;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ThreadLocalRandom;

import static net.initialposition.hardcoremp.util.ConfigOperations.ConfigKeys.RANDOM_CURSE_OF_VANISHING;

public class PlayerItemHeldListener implements Listener {

    JavaPlugin plugin;

    public PlayerItemHeldListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handleEvent(PlayerItemHeldEvent playerItemHeldEvent) {
        int slot = playerItemHeldEvent.getNewSlot();
        ItemStack itemStack = playerItemHeldEvent.getPlayer().getInventory().getItem(slot);

        // Return if the player isn't holding anything
        if (itemStack == null) return;

        // If the item has the curse...
        if (itemStack.containsEnchantment(Enchantment.VANISHING_CURSE)) {
            // Generate a random number 0 - 100
            int randomNumber = ThreadLocalRandom.current().nextInt(0, 101);
            // Get the number to beat
            int vanishChance = this.plugin.getConfig().getInt(String.valueOf(RANDOM_CURSE_OF_VANISHING));
            // Check if we've beaten that number
            if (randomNumber < vanishChance) {
                playerItemHeldEvent.getPlayer().getInventory().clear(slot);
            }
        }
    }

}
