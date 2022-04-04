package net.j4c0b3y.ajlook;

import net.j4c0b3y.ajlook.commands.AjLook;
import net.j4c0b3y.ajlook.config.Config;
import net.j4c0b3y.ajlook.listeners.ServerReloadListener;
import net.j4c0b3y.ajlook.listeners.StartListener;
import net.j4c0b3y.ajlook.utils.PluginUtils;
import net.j4c0b3y.ajlook.utils.ServerUtils;
import org.bukkit.plugin.java.JavaPlugin;
import us.ajg0702.parkour.api.AjPakour;
import us.ajg0702.parkour.game.Manager;

public final class Main extends JavaPlugin {

    public Config config;
    public Manager manager;

    public Boolean isReloaded = false;

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public void onEnable() {
        if (!ServerUtils.isSupported()) {
            getLogger().severe("Your server software is not supported!");
            getLogger().severe("Please use spigot or a fork of spigot");
            getLogger().severe("Disabling.");
            this.setEnabled(false);
            return;
        }

        if (PluginUtils.isInstalled("ajParkour")){
            if(PluginUtils.isEnabled("ajParkour")){
                manager = AjPakour.getManager();
                getLogger().info("Hooked with ajParkour.");
            }else{
                getLogger().warning("ajParkour was found but is not enabled");
                getLogger().warning("Disabling.");
                this.setEnabled(false);
                return;
            }
        }else{
            getLogger().info("ajParkour was not found.");
            getLogger().info("Disabling.");
            this.setEnabled(false);
            return;
        }

        config = new Config(this);

        getServer().getPluginManager().registerEvents(new ServerReloadListener(this), this);
        getServer().getPluginManager().registerEvents(new StartListener(this), this);

        getCommand("ajlook").setExecutor(new AjLook(this));

        getLogger().info("has been ENABLED");
    }

    @Override
    public void onDisable() {
        if(isReloaded){
            getLogger().warning("Reload detected!");
            getLogger().warning("Reloading is not advised and no support will be given if the plugin was reloaded!");
            getLogger().warning("Use /stop or /restart instead.");
            getLogger().info("has been DISABLED, you will receive no support!");
        }else{
            getLogger().info("has been DISABLED");
        }

        getLogger().info("thanks for using " + getDescription().getFullName() + "!");
    }

    public void onReload(){
        config.reload();
        getLogger().info("has been RELOADED!");
    }
}
