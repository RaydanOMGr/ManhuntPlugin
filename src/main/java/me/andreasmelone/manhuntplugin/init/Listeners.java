package me.andreasmelone.manhuntplugin.init;

import me.andreasmelone.manhuntplugin.ManhuntPlugin;
import me.andreasmelone.manhuntplugin.listeners.ManhuntListener;

public class Listeners {
    public static ManhuntListener manhuntListener;

    public static void init(ManhuntPlugin plugin) {
        manhuntListener = new ManhuntListener(plugin);

        plugin.getServer().getPluginManager().registerEvents(manhuntListener, plugin);
    }

    public static void unload(ManhuntPlugin plugin) {
        manhuntListener = null;
    }
}
