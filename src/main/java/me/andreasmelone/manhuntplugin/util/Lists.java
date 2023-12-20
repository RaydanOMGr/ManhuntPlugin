package me.andreasmelone.manhuntplugin.util;

import org.bukkit.inventory.Inventory;

import java.util.*;

public class Lists {
    public static List<UUID> hunterPlayers = new ArrayList<>();
    public static List<UUID> runnerPlayers = new ArrayList<>();
    public static Map<UUID, Inventory> compassInventoryPlayers = new HashMap<>();
}
