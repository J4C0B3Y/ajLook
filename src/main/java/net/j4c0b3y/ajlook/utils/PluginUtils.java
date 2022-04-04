package net.j4c0b3y.ajlook.utils;

import org.bukkit.Bukkit;

public class PluginUtils {

    public static boolean isInstalled(String name) {
        return Bukkit.getPluginManager().getPlugin(name) != null;
    }

    public static boolean isEnabled(String name) {
        return Bukkit.getPluginManager().getPlugin(name).isEnabled();
    }

}
