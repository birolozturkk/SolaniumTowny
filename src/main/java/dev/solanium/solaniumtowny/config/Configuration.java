package dev.solanium.solaniumtowny.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Configuration extends YamlConfiguration {

    private final File file;
    private final JavaPlugin plugin;

    public Configuration(JavaPlugin plugin, String name){
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), name);
    }

    public void create(){
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(file.getName(), false);
        }
        reload();
    }

    public void reload() {
        try {
            load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }
}
