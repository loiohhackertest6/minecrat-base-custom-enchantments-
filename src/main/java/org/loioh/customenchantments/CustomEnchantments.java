package org.loioh.customenchantments;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.loioh.customenchantments.Commands.*;
import org.loioh.customenchantments.Commands.Actions.*;
import org.loioh.customenchantments.Enchantments.EnchantmentsManager;
import org.loioh.customenchantments.Events.EnchatmentConditionEvent;
import org.loioh.customenchantments.Utils.ItemsManager;

import java.util.*;

import org.loioh.customenchantments.Listners.*;

import static org.loioh.customenchantments.Commands.reload.author;
import static org.loioh.customenchantments.Configs.site;
import static org.loioh.customenchantments.Utils.Effects.attrModifier;
import static org.loioh.customenchantments._Init.*;

public final class CustomEnchantments extends JavaPlugin {

    //public static String baseTimer = "25h";

    public static HashMap<String, String> lvls = lvlRoman();
    public static HashMap<String, String> reversedLvls = new HashMap<>();
    public static HashMap<String,String> messages= bM();


    //BASE

    public static Configs lists;

    //GUIS
    public static EnchantmentsManager EM;
    public static Configs enchantments;















    public static Configs config;

    public static CustomEnchantments inst;

    public static CustomEnchantments getInstance(){
        return inst;
    }
    public static Boolean Debug = true;
    public static void msg(String message){
        if(Debug){
            getInstance().getLogger().warning("[LOG]: "+message);
        }
    }

    @Override
    public void onEnable() {
        if (!(getDescription().getAuthors().contains(author) && getDescription().getWebsite().equals(site))) {
            getServer().getPluginManager().disablePlugin(this);
        }

        //Bukkit.spigot().broadcast(new BaseComponent[]{_Init.hl_text(org.bukkit.ChatColor.YELLOW + "[CustomEnchantments]: Welcome!!!", "Author: " + org.bukkit.ChatColor.YELLOW + "" + author + "\n" + org.bukkit.ChatColor.DARK_BLUE + "Click to watch website.", site)});
        Bukkit.spigot().broadcast(new BaseComponent[]{_Init.hl_text(org.bukkit.ChatColor.YELLOW + "[CustomEnchantments]: Welcome!!!"
                , "Author: " + org.bukkit.ChatColor.YELLOW + "" + reload.author    + "\n"
                        + org.bukkit.ChatColor.DARK_BLUE + "Click to visit website." + "\n"
                        + "Licensed for private use only."                           + "\n"
                        + "This plugin is a custom order and is licensed for private use by the commissioner only." + "\n"
                        + "If you find it being sold or distributed on any platform, please contact me immediately." + "\n"
                        + "© Loioh, 2020. All rights reserved."                      + "\n"
                        + "Unauthorized sale, distribution, or modification is prohibited." + "\n"
                , Configs.site)});

        inst = this;
        init();
        //initPackets();

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            loop();
        }, 0L, 1L);
    }

    private void init(){
        EM = new EnchantmentsManager(this);


        Server server = getServer();
        server.getPluginManager().registerEvents(new Listener(), this);
        server.getPluginManager().registerEvents(new EnchantmentsListener(), this);


        registerCommand("ce_reload",new reload(this));

        registerCommand("custom_enchant",new enchant());
        registerCommand("effectfromlist",new effect());

        Debug = false;
        _Init.init(this);

    }
    public static void registerCommand(String command, CommandExecutor executor){
        PluginCommand cmd = Bukkit.getPluginCommand(command);

        if (cmd == null) {
            msg("Command '" + command + "' not found in plugin.yml");
            return;
        }

        cmd.setExecutor(executor);

        if (executor instanceof TabCompleter) {
            cmd.setTabCompleter((TabCompleter)executor);
        }
    }
    public static HashMap<String, Integer> a = new HashMap<>();
    public static Boolean tt(String key,int max){
        int i = 0;
        if(a.containsKey(key)) {
            i = a.get(key);
        }
        i+=1;
        if(i>=max) i=0;

        a.put(key,i);
        return (i==0)? true:false;
    }

    public static HashMap<UUID,Long> notDamageSeconds = new HashMap<>();
    private void loop() {
        int t01 = 1;
        if(tt(t01+"_sec",t01*20)){
            //clearModifiers();

            for(Player p:Bukkit.getOnlinePlayers()) {
                long c = notDamageSeconds.getOrDefault(p.getUniqueId(), 0L);
                if(c<=999) {
                    notDamageSeconds.put(p.getUniqueId(), c + 1);
                }

                new EnchatmentConditionEvent(p,"onSecond");
            }
        }
        new EnchatmentConditionEvent(null,"onTick");
    }

    @Override
    public void onDisable(){
        clearModifiers();
        super.onDisable();
    }

    private void clearModifiers() {
        if(attrModifier!=null && !attrModifier.isEmpty()) {
            for(Map.Entry<UUID,Set<String>> attrMs: attrModifier.entrySet()) {
                UUID uuid = attrMs.getKey();
                Player player = Bukkit.getPlayer(uuid);
                if(player!=null) {
                    for (String s : attrMs.getValue()) {
                        String[] data1 = s.split(":");
                        UUID uid = UUID.fromString(data1[1]);
                        try {
                            AttributeInstance attr = player.getAttribute(Attribute.valueOf(data1[0]));
                            if (attr.getModifier(uid) != null) {
                                attr.removeModifier(uid);
                            }
                        } catch (Exception ex) {
                        }
                    }
                    attrModifier.remove(player.getUniqueId());
                }
            }
        }
    }
}
