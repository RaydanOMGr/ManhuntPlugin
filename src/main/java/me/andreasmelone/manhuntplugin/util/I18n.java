package me.andreasmelone.manhuntplugin.util;

import me.andreasmelone.manhuntplugin.ManhuntPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class I18n {
    private ManhuntPlugin plugin;
    public I18n(ManhuntPlugin plugin) {
        this.plugin = plugin;
    }


    private String language = "en_us";

    public String get(String key) {
        File file = new File(plugin.getDataFolder(), "langs/" + language + ".yml");
        if(!file.exists()) {
            plugin.saveResource("langs/" + language + ".yml", false);
            return key;
        }

        if(!file.exists()) {
            plugin.getLogger().warning("Language file " + language + ".yml does not exist.");
            language = "en_us";
            return get(key);
        }

        FileConfiguration langFile = YamlConfiguration.loadConfiguration(file);
        if(!langFile.isSet(key)) {
            plugin.getLogger().warning("Key " + key + " does not exist in language file " + language + ".yml");
            language = "en_us";
            return get(key);
        }

        return langFile.getString(key);
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
