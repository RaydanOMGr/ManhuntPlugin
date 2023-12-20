package me.andreasmelone.manhuntplugin.init;

import me.andreasmelone.manhuntplugin.ManhuntPlugin;
import me.andreasmelone.manhuntplugin.commands.ManhuntCommand;

public class Commands {
    public static ManhuntCommand manhuntCommand;

    public static void init(ManhuntPlugin plugin) {
        manhuntCommand = new ManhuntCommand(plugin);

        plugin.getCommand("manhunt").setExecutor(manhuntCommand);
        plugin.getCommand("manhunt").setTabCompleter(manhuntCommand);
    }

    public static void unload(ManhuntPlugin plugin) {
        manhuntCommand = null;
        plugin.getCommand("manhunt").setExecutor(null);
        plugin.getCommand("manhunt").setTabCompleter(null);
    }
}
