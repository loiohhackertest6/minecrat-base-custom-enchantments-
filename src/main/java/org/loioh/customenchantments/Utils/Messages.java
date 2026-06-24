package org.loioh.customenchantments.Utils;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.loioh.customenchantments.CustomEnchantments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.loioh.customenchantments._Init.list;
import static org.loioh.customenchantments._Init.tryParse;

public class Messages {
     public static final String players_command_parameter = "<player_name>";

     public static void sendMessage(CommandSender sender, String msg) {
         String message = getMessage(msg);
         if (message != null) {
             sendMessage0(sender, message);
         }
     }

     public static void sendMessage(CommandSender sender, String msg, HashMap<String, String> placeholders) {
         String message = getMessage(msg, placeholders);
         if (message != null) {
             sendMessage0(sender, message);
         }
     }

     private static void sendMessage0(CommandSender sender, String message) {
         if (message == null || message.equals("")) return;
         BaseComponent[] bc = tryParse(message);
         boolean sendBase = true;
         //CustomEnchantments.msg("Bc:"+bc.toString()+" : "+message);
         if (bc != null && bc.length > 0 && bc.toString() != message) {
             try {
                 sender.sendMessage(bc);
                 sendBase = false;
             } catch (NullPointerException ex) {
             }
         }
         if (sendBase) {
             sender.sendMessage(message);
         }
     }

     public static String getMessage(String msg, HashMap<String, String> placeholders) {
         String message = getMessage(msg);
         if (message == null) message = "";
         return fixPlaceholders(message, placeholders);
     }

     public static String getMessage(String msg) {
         return getMessage(msg, "");
     }

     public static String getMessage(String msg, String def) {
         //CustomEnchantments.msg("msg: "+msg);
         if (CustomEnchantments.messages != null && !CustomEnchantments.messages.isEmpty()) {
             //CustomEnchantments.msg("messages: "+true);
             msg = msg.replace(".",",");
             if (CustomEnchantments.messages.containsKey(msg)) {
                 String message = CustomEnchantments.messages.get(msg);
                 //CustomEnchantments.msg("messages: "+message);
                 return message.replace("&", "§");
             }
         }
         return def;
     }


     public static String fixPlaceholder(String message, String placeholder, String value) {
         List<String> forms = list("%" + placeholder + "%", "<" + placeholder + ">");
         for (String form : forms) {
             if (message.contains(form)) {
                 message = message.replace(form, value);
             }
         }
         return message;
     }

    public static String fixPlaceholders(String message, HashMap<String, String> placeholders) {
        if (placeholders != null && !placeholders.isEmpty()) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                message = fixPlaceholder(message, entry.getKey(), entry.getValue());
            }
        }
        return message;
    }
 }
    

