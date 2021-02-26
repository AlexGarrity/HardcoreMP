package de.initialposition.hardcoremp.listeners;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.initialposition.hardcoremp.models.PlayerLogout;
import de.initialposition.hardcoremp.util.ConsoleLogger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

import static de.initialposition.hardcoremp.util.ConfigOperations.ConfigKeys.*;

public class PlayerLoginOutListener implements Listener {

    private final JavaPlugin plugin;

    private final Gson gson;

    private ArrayList<PlayerLogout> logoutList;

    private final int RANDOM_MESSAGE_COUNT = 20;

    private final ArrayList<String> loginMessages = new ArrayList<>();

    public PlayerLoginOutListener(JavaPlugin plugin) {
        this.plugin = plugin;

        gson = new Gson();

        this.logoutList = new ArrayList<>();

        // add messages to arraylist
        this.loginMessages.add("BTW PLAYER made your items go poof");
        this.loginMessages.add("If you needed your items, too bad, PLAYER took them away");
        this.loginMessages.add("PLAYER messed up and now your inventory is empty");
        this.loginMessages.add("Maybe you should ask PLAYER where your items went...");
        this.loginMessages.add("PLAYER uses inventory clear! It is very effective!");
        this.loginMessages.add("\"Empty inventories are fair and balanced\" - PLAYER");
        this.loginMessages.add("It wasn't me, it was PLAYER!");
        this.loginMessages.add("And now, PLAYER the magician will make your items disappear!");
        this.loginMessages.add("PLAYER decided that you didn't even need those items.");
        this.loginMessages.add("PLAYER: 1. Items: 0.");
        this.loginMessages.add("At some point in the recent past, PLAYER died. This might affected you.");
        this.loginMessages.add("You might want to have a talk with PLAYER.");
        this.loginMessages.add("Your items are gone. Thank PLAYER.");
        this.loginMessages.add("My name is PLAYER and I deleted your inventory.");
        this.loginMessages.add("Bing Bong your inventory is gone. (It was PLAYER.)");
        this.loginMessages.add("I don't want to imply anything, I'm just saying PLAYER was the last person that died.");
        this.loginMessages.add("Welcome to The Empty Inventory Adventures II: Revenge of PLAYER");
        this.loginMessages.add("In case you missed it, your inventory is now empty. PLAYER may be the one to blame.");
        this.loginMessages.add("PLAYER snapped the Inventorinity Gauntlet twice, deleting 100% of items.");
        this.loginMessages.add("PLAYER has taken your inventory. You may scream at them once.");
        this.loginMessages.add("PLAYER stole your items. Kill them. no wait no dont");
    }

    @EventHandler
    public void handleLogin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        ConsoleLogger.consoleLog(p.getDisplayName() + " logged in");

        PlayerLogout logoutData = null;

        // loop through the known logouts and see if the player has data
        for (PlayerLogout logout : this.logoutList) {

            if (logout.getUUID().equals(p.getUniqueId())) {
                // data found! load it and break
                logoutData = logout;
                break;
            }
        }

        // only try to delete if player logged in before AND inventory deletion is enabled
        if (logoutData != null && this.plugin.getConfig().getBoolean(String.valueOf(DELETE_INVENTORIES_ON_DEATH))) {
            // check if someone died since the player logged in
            if (logoutData.getLastLogin() < this.plugin.getConfig().getLong(String.valueOf(LAST_DEATH_TIME))) {
                // if yes, clear inventory
                p.getInventory().clear();

                // send a message to tell the player who deleted their shiny items
                // get one of the random messages
                int randNr = (int) (Math.random() * RANDOM_MESSAGE_COUNT);
                String inventoryGoneMessage = this.loginMessages.get(randNr);

                // insert the dying players name
                String lastDeathName = this.plugin.getConfig().getString(String.valueOf(LAST_DEATH_NAME));
                if (lastDeathName != null) {
                    inventoryGoneMessage = inventoryGoneMessage.replace("PLAYER", lastDeathName);
                }

                // send the message to the logged in user
                p.sendMessage(ChatColor.RED + inventoryGoneMessage);
            }

            // remove login data from the list (makes relogging faster)
            ConsoleLogger.consoleLog("Removing " + p.getDisplayName() + " from list.");
            this.logoutList.remove(logoutData);
        } else {
            ConsoleLogger.consoleLog("No saved logout data for this player found.");
        }
    }

    @EventHandler
    public void handleLogout(PlayerQuitEvent event) {
        // get the player that logged out
        Player p = event.getPlayer();
        ConsoleLogger.consoleLog(p.getDisplayName() + " logged out");

        // save new player logout info
        PlayerLogout lastPlayerLogout = new PlayerLogout(p.getUniqueId());
        this.logoutList.add(lastPlayerLogout);
    }

    public void savePlayerOfflineList() {
        ConsoleLogger.consoleLog("Saving offline player list...");

        // convert list to string for easier saving
        String convertedList = this.gson.toJson(this.logoutList);

        this.plugin.getConfig().set(String.valueOf(OFFLINE_PLAYERS), convertedList);
        this.plugin.saveConfig();
    }

    public void loadPlayerOfflineList() {
        ConsoleLogger.consoleLog("Loading offline player list...");

        // load list string and convert back to list
        String loadedList = this.plugin.getConfig().getString(String.valueOf(OFFLINE_PLAYERS));
        this.logoutList = gson.fromJson(loadedList, new TypeToken<ArrayList<PlayerLogout>>() {
        }.getType());
    }
}
