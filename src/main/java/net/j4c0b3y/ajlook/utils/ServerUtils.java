package net.j4c0b3y.ajlook.utils;

import org.bukkit.Bukkit;

public class ServerUtils {

    public static boolean isSupported() {
        try {
            Class.forName("net.md_5.bungee.api.ChatColor");
            return true;
        } catch (ClassNotFoundException error) {
            return false;
        }
    }

    @SuppressWarnings("unused")
    public static void stop() {
        Bukkit.getServer().shutdown();
    }

    @SuppressWarnings("unused")
    public static void restart() {
        Bukkit.getServer().spigot().restart();
    }

}
