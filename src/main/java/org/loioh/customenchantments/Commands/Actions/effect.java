package org.loioh.customenchantments.Commands.Actions;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.loioh.customenchantments.CustomEnchantments;
import org.loioh.customenchantments.Utils.Effects;
import org.loioh.customenchantments.Utils.Messages;
import org.loioh.customenchantments.Utils.Players;

import java.util.ArrayList;
import java.util.List;

import static org.loioh.customenchantments.CustomEnchantments.msg;
import static org.loioh.customenchantments.Utils.TimeConvert.*;
import static org.loioh.customenchantments._Init.hashMap;
import static org.loioh.customenchantments._Init.list;

public class effect implements CommandExecutor, TabCompleter {
    public static final String author="loioh";

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("effectfl") || command.getName().equalsIgnoreCase("effectfromlist") || command.getName().equalsIgnoreCase("effectFL")){
            if ((sender).isOp() || ((Player) sender).hasPermission("customenchantments.commands.effect")) {
                if(args.length<2){
                    Messages.sendMessage(sender,"commands.args_amount.denied");
                    return true;
                }
                Player player = Players.getPlayer(args[0]);
                if(player!=null){
                    msg("starting calculationEffects");
                    String list = args[1];
                    int duration = 5;
                    if(args.length>=3){
                        duration = safeDoubleToInt(calculateSafe(args[2]));
                    }

                    msg("adding effect");
                    boolean success = Effects.applyRandomEffectFromList(player,list,duration);
                    if(!success){
                        Messages.sendMessage(sender,"commands.not_recognized_path.denied",hashMap("arg",args[1]));
                    }
                }else{
                    Messages.sendMessage(sender,"commands.not_recognized_player.denied",hashMap("arg",args[0]));
                }
            }else{
                Messages.sendMessage(sender,"commands.permission.denied");
            }
            return  true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if(args.length == 1){
            for(Player p : Bukkit.getOnlinePlayers()){
                list. add(p.getName());
            }
        }
        if(args.length==2){
            list.addAll(CustomEnchantments.lists.getCS("Effects"));
        }
        if(args.length==3){
            list.addAll(list("10","60","3600"));
        }
        return list;
    }
}
