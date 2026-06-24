package org.loioh.customenchantments.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.loioh.customenchantments.Utils.Messages;
import org.loioh.customenchantments._Init;


public class reload  implements CommandExecutor {
    static JavaPlugin plugin;

    public reload(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    public static final String author="loioh";

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("ce_reload") || command.getName().equalsIgnoreCase("ce_r") || command.getName().equalsIgnoreCase("cer")){
            if (!(sender instanceof Player) || (sender).isOp() || ((Player) sender).hasPermission("customenchantments.commands.reload")) {
                _Init.init(plugin);
                Messages.sendMessage(sender,"commands.reload.success");
            }else{
                Messages.sendMessage(sender,"commands.permission.denied");
            }
            return  true;
        }
        return false;
    }
}
