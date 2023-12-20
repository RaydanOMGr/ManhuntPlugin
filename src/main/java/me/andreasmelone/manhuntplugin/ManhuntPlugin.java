package me.andreasmelone.manhuntplugin;

import me.andreasmelone.manhuntplugin.init.Commands;
import me.andreasmelone.manhuntplugin.init.Listeners;
import me.andreasmelone.manhuntplugin.util.I18n;
import org.bukkit.plugin.java.JavaPlugin;

public final class ManhuntPlugin extends JavaPlugin {
    private final I18n i18n = new I18n(this);
    public boolean isRunning = false;
    public long startTime = 0;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        i18n.setLanguage(getConfig().getString("language"));
        Commands.init(this);
        Listeners.init(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Commands.unload(this);
        Listeners.unload(this);
    }

    public I18n getI18n() {
        return i18n;
    }

    public static ManhuntPlugin getInstance() {
        return getPlugin(ManhuntPlugin.class);
    }
}
