package net.j4c0b3y.ajlook.listeners;

import net.j4c0b3y.ajlook.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class ServerReloadListener implements Listener {

    public Main plugin;

    public ServerReloadListener(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerReload(ServerCommandEvent event){
        if(event.getCommand().toLowerCase().startsWith("reload")) plugin.isReloaded = true;
    }

    @EventHandler
    public void onRemoteServerReload(RemoteServerCommandEvent event){
        if(event.getCommand().toLowerCase().startsWith("reload")) plugin.isReloaded = true;
    }

    @EventHandler
    public void onPlayerReload(PlayerCommandPreprocessEvent event){
        if(event.getMessage().toLowerCase().startsWith("/reload") && event.getPlayer().hasPermission("bukkit.command.reload")) plugin.isReloaded = true;
    }
}