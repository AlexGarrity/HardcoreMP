package net.initialposition.hardcoremp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import static net.initialposition.hardcoremp.util.ConfigOperations.ConfigKeys;

public class CommandHandler implements CommandExecutor {

    JavaPlugin plugin;

    public CommandHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private void getValue(CommandSender commandSender, String[] arguments) {
        if (arguments.length != 2) return;
        Object value = plugin.getConfig().get(String.valueOf(arguments[1]));
        if (value == null) {
            commandSender.sendMessage("Value " + arguments[1] + " is not valid");
        }
        else {
            commandSender.sendMessage(arguments[1] + ": " + value.toString());
        }
    }

    private void setValue(CommandSender commandSender, String[] arguments) {
        if (arguments.length != 3) return;
        Object value = plugin.getConfig().get(String.valueOf(arguments[1]));
        if (value == null) {
            commandSender.sendMessage("Value " + arguments[1] + " is not valid");
        }
        else {
            // Try to set it as a boolean
            if (arguments[2].equalsIgnoreCase("true")) {
                plugin.getConfig().set(String.valueOf((arguments[1])), true);
            }
            else if (arguments[2].equalsIgnoreCase("false")) {
                plugin.getConfig().set(String.valueOf((arguments[1])), false);
            }
            else {
                try {
                    // Try to set it as an integer
                    int v = Integer.parseInt(arguments[2]);
                    plugin.getConfig().set(String.valueOf((arguments[1])), v);
                }
                catch(NumberFormatException ignored) {
                    // Not a number so treat it as a string
                    plugin.getConfig().set(String.valueOf((arguments[1])), arguments[2]);
                }
            }
        }
        commandSender.sendMessage(arguments[1] + ": " + plugin.getConfig().get(String.valueOf(arguments[1])));
    }

    private void listValues(CommandSender commandSender) {
        for (ConfigKeys key : ConfigKeys.values()) {
            Object value = plugin.getConfig().get(String.valueOf(key));
            if (value != null)
                commandSender.sendMessage(key.name() + ": " + value.toString());
        }
    }

    private void printHelpMessage(CommandSender commandSender) {
        commandSender.sendMessage("Description: HardcoreMP config command");
        commandSender.sendMessage("Usage: hardcoremp");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] arguments) {
        // If no arguments are provided, print a help message
        if (arguments.length == 0) {
            printHelpMessage(commandSender);
            return false;
        }

        String firstArgument = arguments[0];
        switch (firstArgument) {
            case "help":
                printHelpMessage(commandSender);
                break;
            case "list":
                listValues(commandSender);
                break;
            case "set":
                setValue(commandSender, arguments);
                break;
            case "get":
                getValue(commandSender, arguments);
                break;
        }

        return true;
    }
}
