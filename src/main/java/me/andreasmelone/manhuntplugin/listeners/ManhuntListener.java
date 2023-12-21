package me.andreasmelone.manhuntplugin.listeners;

import me.andreasmelone.manhuntplugin.ManhuntPlugin;
import me.andreasmelone.manhuntplugin.items.abstracts.SpecialItems;
import me.andreasmelone.manhuntplugin.util.Lists;
import me.andreasmelone.manhuntplugin.util.TranslationKey;
import me.andreasmelone.manhuntplugin.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Objects;

public class ManhuntListener implements Listener {
    private final ManhuntPlugin plugin;
    public ManhuntListener(ManhuntPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(!plugin.isRunning) return;
        Player player = event.getEntity();

        if(Lists.hunterPlayers.contains(player.getUniqueId())) {
            if (event.getKeepInventory()) return;

            List<ItemStack> drops = event.getDrops();
            for (ItemStack drop : drops) {
                if (SpecialItems.TRACK_COMPASS.isSameSpecialItem(drop)) {
                    event.getDrops().remove(drop);
                }
            }
        } else if(Lists.runnerPlayers.contains(player.getUniqueId())) {
            Lists.spectatorPlayers.add(player.getUniqueId());
            Bukkit.broadcastMessage(
                    Util.transform(plugin.getI18n().get("runner_death", TranslationKey.of("%player%", player.getDisplayName())))
            );
            if(Lists.runnerPlayers.size() == Lists.spectatorPlayers.size()) {
                Bukkit.broadcastMessage(
                        Util.transform(plugin.getI18n().get("all_runners_dead"))
                );
                Bukkit.broadcastMessage(
                        Util.transform(plugin.getI18n().get("game_lasted", TranslationKey.of("%minutes%", (System.currentTimeMillis() - plugin.startTime) / 1000 / 60)))
                );

                plugin.isRunning = false;
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if(!plugin.isRunning) return;
        Player player = event.getPlayer();

        if(Lists.hunterPlayers.contains(player.getUniqueId())) {
            player.getInventory().addItem(SpecialItems.TRACK_COMPASS.getItemStack());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!plugin.isRunning) return;
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(item == null) return;

        if(SpecialItems.TRACK_COMPASS.isSameSpecialItem(item)) {
            if(Lists.hunterPlayers.contains(player.getUniqueId())) {
                if(player.getCooldown(Material.COMPASS) > 0) {
                    player.sendMessage(Util.transform(plugin.getI18n().get("track_compass_cant_use")));
                    player.sendMessage(Util.transform(plugin.getI18n().get("track_compass_cooldown").replace("%seconds%", String.valueOf(player.getCooldown(Material.COMPASS) / 20))));
                    return;
                }

                if(Lists.runnerPlayers.size() > 1) {
                    Inventory inv = plugin.getServer().createInventory(null, 3 * 9, plugin.getI18n().get("players"));
                    inv.setContents(
                            Lists.runnerPlayers.stream().map(uuid -> {
                                Player target = Bukkit.getPlayer(uuid);
                                if (target == null) return null;
                                ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);

                                SkullMeta skull = (SkullMeta) itemStack.getItemMeta();
                                if (skull == null) return itemStack;
                                skull.setOwningPlayer(target);
                                skull.setDisplayName(target.getDisplayName());
                                itemStack.setItemMeta(skull);

                                return itemStack;
                            }).filter(Objects::nonNull).toArray(ItemStack[]::new)
                    );
                    Lists.compassInventoryPlayers.put(player.getUniqueId(), inv);

                    player.openInventory(inv);
                } else {
                    Player target = Bukkit.getPlayer(Lists.runnerPlayers.get(0));
                    if(target == null) return;
                    player.setCompassTarget(target.getLocation());
                    player.sendMessage(
                            Util.transform(
                                    plugin.getI18n().get(
                                            "track_compass_pointing_to",
                                            TranslationKey.of("%player%", target.getDisplayName())
                                    )
                            )
                    );
                    if(player.getWorld() == target.getWorld())
                        player.sendMessage(
                                Util.transform(
                                        plugin.getI18n().get(
                                                "player_distance_away",
                                                TranslationKey.of("%distance%", (int) player.getLocation().distance(target.getLocation()))
                                        )
                                )
                        );
                    else
                        player.sendMessage(
                                Util.transform(
                                        plugin.getI18n().get(
                                                "player_another_dimension",
                                                TranslationKey.of("%player%", target.getDisplayName())
                                        )
                                )
                        );
                    player.sendMessage(
                            Util.transform(
                                    plugin.getI18n().get(
                                            "player_height",
                                            TranslationKey.of("%player%", target.getDisplayName()),
                                            TranslationKey.of("%height%", (int) target.getLocation().getY())
                                    )
                            )
                    );

                    player.setCooldown(Material.COMPASS, 20 * 20);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!plugin.isRunning) return;
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        ItemStack item = event.getCurrentItem();

        if(item == null) return;

        if(Lists.compassInventoryPlayers.containsKey(player.getUniqueId())) {
            if(inv == Lists.compassInventoryPlayers.get(player.getUniqueId())) {
                if(item.getType().equals(Material.PLAYER_HEAD)) {
                    event.setCancelled(true);

                    SkullMeta skull = (SkullMeta) item.getItemMeta();
                    if(skull == null) return;
                    if(skull.getOwningPlayer() == null) return;

                    Player target = Bukkit.getPlayer(skull.getOwningPlayer().getUniqueId());
                    if(target == null) return;
                    player.setCompassTarget(target.getLocation());
                    player.sendMessage(
                            Util.transform(
                                    plugin.getI18n().get(
                                            "track_compass_pointing_to",
                                            TranslationKey.of("%player%", target.getDisplayName())
                                    )
                            )
                    );
                    if(player.getWorld() == target.getWorld())
                        player.sendMessage(
                                Util.transform(
                                        plugin.getI18n().get(
                                                "player_distance_away",
                                                TranslationKey.of("%distance%", (int) player.getLocation().distance(target.getLocation()))
                                        )
                                )
                        );
                    else
                        player.sendMessage(
                                Util.transform(
                                        plugin.getI18n().get(
                                                "player_another_dimension",
                                                TranslationKey.of("%player%", target.getDisplayName())
                                        )
                                )
                        );
                    player.sendMessage(
                            Util.transform(
                                    plugin.getI18n().get(
                                            "player_height",
                                            TranslationKey.of("%player%", target.getDisplayName()),
                                            TranslationKey.of("%height%", (int) target.getLocation().getY())
                                    )
                            )
                    );

                    player.setCooldown(Material.COMPASS, 20 * 20);
                    player.closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!plugin.isRunning) return;
        Player player = (Player) event.getPlayer();
        Inventory inv = event.getInventory();

        if(Lists.compassInventoryPlayers.containsKey(player.getUniqueId())) {
            if(inv == Lists.compassInventoryPlayers.get(player.getUniqueId())) {
                Lists.compassInventoryPlayers.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onPlayerKillEntity(EntityDeathEvent event) {
        if(!plugin.isRunning) return;

        if(event.getEntity().getKiller() == null) return;
        Player player = event.getEntity().getKiller();
        if(event.getEntity() instanceof EnderDragon) {
                Bukkit.broadcastMessage(
                        Util.transform(
                                plugin.getI18n().get(
                                        "player_killed_dragon",
                                        TranslationKey.of("%player%", player.getDisplayName())
                                )
                        )
                );
                Bukkit.broadcastMessage(
                        Util.transform(
                                plugin.getI18n().get(
                                        "game_lasted",
                                        TranslationKey.of("%minutes%", (System.currentTimeMillis() - plugin.startTime) / 1000 / 60)
                                )
                        )
                );
                Lists.hunterPlayers.forEach(uuid -> {
                    Player hunter = Bukkit.getPlayer(uuid);
                    if(hunter == null) return;
                    hunter.sendMessage(
                            Util.transform(
                                    // hunters, you still can kill the runners until they enter the portal!
                                    plugin.getI18n().get(
                                            "hunters_can_still_kill_runners"
                                    )
                            )
                    );
                });
        }
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        if(!plugin.isRunning) return;

        Player player = event.getPlayer();
        if(event.getFrom().getEnvironment().equals(World.Environment.THE_END)) {
            if(Lists.runnerPlayers.contains(player.getUniqueId())) {
                Bukkit.broadcastMessage(
                        Util.transform(
                                plugin.getI18n().get(
                                        "runner_entered_portal",
                                        TranslationKey.of("%player%", player.getDisplayName())
                                )
                        )
                );
                Bukkit.broadcastMessage(
                        Util.transform(
                                plugin.getI18n().get(
                                        "game_lasted",
                                        TranslationKey.of("%minutes%", (System.currentTimeMillis() - plugin.startTime) / 1000 / 60)
                                )
                        )
                );

                plugin.isRunning = false;
            }
        }
    }

//    @EventHandler
//    public void onPlayerPortal(PlayerPortalEvent event) {
//        Player player = event.getPlayer();
//        if(event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
//            if(event.getTo().getWorld().getEnvironment().equals(World.Environment.NORMAL)
//                    && event.getFrom().getWorld().getEnvironment().equals(World.Environment.THE_END)) {
//                if(Lists.runnerPlayers.contains(player.getUniqueId())) {
//                    Bukkit.broadcastMessage(
//                            Util.transform("&b" + player.getDisplayName() + " entered the portal! The runners win!")
//                    );
//                    Bukkit.broadcastMessage(
//                            Util.transform("&bThe game lasted " + (System.currentTimeMillis() - plugin.startTime) / 1000 + " seconds")
//                    );
//
//                    Lists.runnerPlayers.clear();
//                    Lists.hunterPlayers.clear();
//
//                    plugin.isRunning = false;
//                }
//            }
//        }
//    }
}
