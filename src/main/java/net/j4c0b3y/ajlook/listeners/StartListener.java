package net.j4c0b3y.ajlook.listeners;

import net.j4c0b3y.ajlook.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;
import us.ajg0702.parkour.api.events.PlayerStartParkourEvent;

public class StartListener implements Listener {

    public Main plugin;

    public StartListener(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onStart(PlayerStartParkourEvent event){
        Player player = event.getPlayer();

        Location block = event.getParkourPlayer().getJumps().get(1).getTo().clone();

        block.setY(block.getBlockY() + 0.5 + plugin.config.getDouble("y-offset"));

        Vector direction = block.subtract(player.getEyeLocation()).toVector();

        player.teleport(player.getLocation().setDirection(direction));
    }

}
