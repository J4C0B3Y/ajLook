package net.j4c0b3y.ajlook.config;

import net.j4c0b3y.ajlook.utils.ConfigFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@SuppressWarnings("unused")
public class Config {
    ConfigFile configFile;

    public Config(JavaPlugin plugin) {
        this.configFile = new ConfigFile(plugin, "config.yml");
    }

    public Object get(String key) {
        return this.configFile.get(key);
    }

    public List<ConfigurationSection> getSections(String path) {
        return this.configFile.getSections(path);
    }

    public ConfigurationSection getSection(String path) {
        return this.configFile.getSection(path);
    }

    public Integer getInt(String key) {
        return this.configFile.getInt(key);
    }

    public Double getDouble(String key) {
        return this.configFile.getDouble(key);
    }

    public String getString(String key) {
        return this.configFile.getString(key);
    }

    public List<String> getStringList(String key) {
        return this.configFile.getStringList(key);
    }

    public boolean getBoolean(String key) {
        return this.configFile.getBoolean(key);
    }

    public void reload() {
        this.configFile.reload();
    }
}
