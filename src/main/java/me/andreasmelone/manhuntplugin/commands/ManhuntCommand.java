package me.andreasmelone.manhuntplugin.commands;

import me.andreasmelone.manhuntplugin.ManhuntPlugin;
import me.andreasmelone.manhuntplugin.items.abstracts.SpecialItems;
import me.andreasmelone.manhuntplugin.util.Lists;
import me.andreasmelone.manhuntplugin.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManhuntCommand implements TabExecutor {
    private final ManhuntPlugin plugin;
    public ManhuntCommand(ManhuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 0) return false;
        if (strings[0].equalsIgnoreCase("add")) {
            if (!commandSender.hasPermission("manhunt.add")) {
                commandSender.sendMessage(Util.transform(plugin.getI18n().get("no_permission")));
                return true;
            }
            if (strings.length == 1) {
                commandSender.sendMessage(Util.transform(plugin.getI18n().get("specify_team")));
                return true;
            }
            if (strings.length == 2) {
                commandSender.sendMessage(Util.transform(plugin.getI18n().get("specify_player")));
                return true;
            }
            if (strings[1].equalsIgnoreCase("hunter")) {
                Player target = Bukkit.getPlayer(strings[2]);
                if (target == null) {
                    commandSender.sendMessage(Util.transform(plugin.getI18n().get("player_not_online").replaceAll("%player%", strings[2])));
                    return true;
                }
                if(Lists.hunterPlayers.contains(target.getUniqueId())) {
                    commandSender.sendMessage(Util.transform(plugin.getI18n().get("player_already_hunter").replaceAll("%player%", target.getDisplayName())));
                    return true;
                }
                Lists.hunterPlayers.add(target.getUniqueId());
                commandSender.sendMessage(Util.transform(plugin.getI18n().get("player_now_hunter").replaceAll("%player%", target.getDisplayName())));
                target.sendMessage(Util.transform(plugin.getI18n().get("you_now_hunter")));
            } else if (strings[1].equalsIgnoreCase("runner")) {
                Player target = Bukkit.getPlayer(strings[2]);
                if (target == null) {
                    commandSender.sendMessage(Util.transform(plugin.getI18n().get("player_not_online").replaceAll("%player%", strings[2])));
                    return true;
                }
                if(Lists.runnerPlayers.contains(target.getUniqueId())) {
                    commandSender.sendMessage(Util.transform(plugin.getI18n().get("player_already_runner").replaceAll("%player%", target.getDisplayName())));
                    return true;
                }
                Lists.runnerPlayers.add(target.getUniqueId());
                commandSender.sendMessage(Util.transform(plugin.getI18n().get("player_now_runner").replaceAll("%player%", target.getDisplayName())));
                target.sendMessage(Util.transform(plugin.getI18n().get("you_now_runner")));
            } else {
                commandSender.sendMessage(Util.transform(plugin.getI18n().get("specify_valid_team")));
                return true;
            }
        } else if (strings[0].equalsIgnoreCase("remove")) {
            if (!commandSender.hasPermission("manhunt.remove")) {
                commandSender.sendMessage(Util.transform(plugin.getI18n().get("no_permission")));
                return true;
            }
            if (strings.length == 1) {
                commandSender.sendMessage(Util.transform(plugin.getI18n().get("specify_team")));
                return true;
            }
            if (strings.length == 2) {
                commandSender.sendMessage(Util.transform(plugin.getI18n().get("specify_player")));
                return true;
            }
            if (strings[1].equalsIgnoreCase("hunter")) {
                Player target = Bukkit.getPlayer(strings[2]);
                if (target == null) {
                    commandSender.sendMessage(Util.transform(plugin.getI18n().get("player_not_online").replaceAll("%player%", strings[2])));
                    return true;
                }
                if(!Lists.hunterPlayers.contains(target.getUniqueId())) {
                    commandSender.sendMessage(Util.transform(plugin.getI18n().get("player_not_hunter").replaceAll("%player%", target.getDisplayName())));
                    return true;
                }
                Lists.hunterPlayers.remove(target.getUniqueId());
                commandSender.sendMessage(Util.transform(plugin.getI18n().get("player_no_longer_hunter").replaceAll("%player%", target.getDisplayName())));
                target.sendMessage(Util.transform(plugin.getI18n().get("no_longer_hunter")));
            } else if (strings[1].equalsIgnoreCase("runner")) {
                Player target = Bukkit.getPlayer(strings[2]);
                if (target == null) {
                    commandSender.sendMessage(Util.transform(plugin.getI18n().get("player_not_online").replaceAll("%player%", strings[2])));
                    return true;
                }
                if(!Lists.runnerPlayers.contains(target.getUniqueId())) {
                    commandSender.sendMessage(Util.transform(plugin.getI18n().get("player_not_runner").replaceAll("%player%", target.getDisplayName())));
                    return true;
                }
                Lists.runnerPlayers.remove(target.getUniqueId());
                commandSender.sendMessage(Util.transform(plugin.getI18n().get("player_no_longer_runner").replaceAll("%player%", target.getDisplayName())));
                target.sendMessage(Util.transform(plugin.getI18n().get("no_longer_runner")));
            } else {
                commandSender.sendMessage(Util.transform(plugin.getI18n().get("specify_valid_team")));
                return true;
            }
        } else if (strings[0].equalsIgnoreCase("start")) {
            if (!commandSender.hasPermission("manhunt.start")) {
                commandSender.sendMessage(Util.transform(plugin.getI18n().get("no_permission")));
                return true;
            }
            if(Lists.runnerPlayers.size() == 0) {
                commandSender.sendMessage(Util.transform(plugin.getI18n().get("no_runners")));
                return true;
            }
            if(Lists.hunterPlayers.size() == 0) {
                commandSender.sendMessage(Util.transform(plugin.getI18n().get("no_hunters")));
                return true;
            }

            plugin.isRunning = true;
            plugin.startTime = System.currentTimeMillis();

            plugin.getServer().getWorlds().forEach(world -> world.setTime(0));
            plugin.getServer().getWorlds().forEach(world -> world.setWeatherDuration(0));

            Bukkit.broadcastMessage(Util.transform(plugin.getI18n().get("game_started")));
            Lists.runnerPlayers.forEach(uuid -> {
                Player player = Bukkit.getPlayer(uuid);
                if(player == null) return;
                player.sendMessage(Util.transform(plugin.getI18n().get("you_are_runner")));

                player.getInventory().clear();
                for(PotionEffectType effectType : PotionEffectType.values()) {
                    if(effectType == null) continue;
                    player.removePotionEffect(effectType);
                }
                player.setHealth(20);
                player.setFoodLevel(20);
                player.setSaturation(20);
            });

            Lists.hunterPlayers.forEach(uuid -> {
                Player player = Bukkit.getPlayer(uuid);
                if(player == null) return;
                player.sendMessage(Util.transform(plugin.getI18n().get("you_are_hunter")));

                player.getInventory().clear();
                player.setHealth(20);
                player.setFoodLevel(20);
                player.setSaturation(20);

                player.getInventory().addItem(SpecialItems.TRACK_COMPASS.getItemStack());
            });
        } else if (strings[0].equalsIgnoreCase("reload")) {
            if (!commandSender.hasPermission("manhunt.reload")) {
                commandSender.sendMessage(Util.transform(plugin.getI18n().get("no_permission")));
                return true;
            }
            plugin.reloadConfig();
            plugin.getI18n().setLanguage(plugin.getConfig().getString("language"));
            commandSender.sendMessage(Util.transform(plugin.getI18n().get("reloaded")));
        } else {
            return false;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> subCommands = new ArrayList<>();
        if(strings.length == 1) {
            if(commandSender.hasPermission("manhunt.add")) subCommands.add("add");
            if(commandSender.hasPermission("manhunt.remove")) subCommands.add("remove");
            if(commandSender.hasPermission("manhunt.start")) subCommands.add("start");
            if(commandSender.hasPermission("manhunt.reload")) subCommands.add("reload");
        } else if(strings.length == 2) {
            if(strings[0].equalsIgnoreCase("add") && commandSender.hasPermission("manhunt.add")
                    || strings[0].equalsIgnoreCase("remove") && commandSender.hasPermission("manhunt.remove")) {
                subCommands.add("hunter");
                subCommands.add("runner");
            }
        } else if(strings.length == 3) {
            if(strings[0].equalsIgnoreCase("add") || strings[0].equalsIgnoreCase("remove")) {
                return new ArrayList<>(Util.getOnlinePlayersNamesListWithArgument(strings[2]));
            }
        }

        return subCommands;
    }
}
