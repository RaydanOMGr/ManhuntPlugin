package me.andreasmelone.manhuntplugin.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static String transform(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    /**
     * Gets a list of all online players names
     * @return The list of all online players names
     */
    public static List<String> getOnlinePlayersNamesList() {
        return getOnlinePlayersNamesListWithArgument(null); // Call the other method with null as argument
    }

    /**
     * Gets a list of all online players names that start with the specified string
     * @param argument The string the names should start with
     * @return The list of all online players names that start with the specified string
     */
    public static List<String> getOnlinePlayersNamesListWithArgument(String argument) {
        List<String> list = new ArrayList<>(); // Create a new list
        for(Player player : Bukkit.getServer().getOnlinePlayers()) { // Loop through all online players
            if(argument == null || player.getName().toLowerCase().startsWith(argument.toLowerCase())) list.add(player.getName()); // If the argument is null or the player name starts with the argument, add the player name to the list
        }
        return list; // Return the list
    }

}
