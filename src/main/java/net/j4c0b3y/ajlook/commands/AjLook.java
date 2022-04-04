package net.j4c0b3y.ajlook.commands;

import net.j4c0b3y.ajlook.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AjLook implements CommandExecutor {

    public Main plugin;

    public AjLook(Main plugin){
        this.plugin = plugin;
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("reload")){
            if(sender instanceof Player){
                Player player = (Player) sender;

                if(player.hasPermission("ajparkour.reload")){
                    playerReload(player);
                }else{
                    noPermission(player);
                }
            }else{
                plugin.onReload();
            }
        }else{
            unknownCommand(sender);
            return true;
        }

        return false;
    }

    public void noPermission(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("no-permission")));
    }

    public void unknownCommand(CommandSender sender){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("unknown-command")));
    }

    public void playerReload(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("reload")));
        plugin.onReload();
    }
}
