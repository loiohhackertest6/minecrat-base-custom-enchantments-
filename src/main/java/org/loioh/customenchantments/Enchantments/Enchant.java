package org.loioh.customenchantments.Enchantments;

import net.md_5.bungee.api.chat.BaseComponent;
import org.loioh.customenchantments.Configs;
import org.loioh.customenchantments._Init;

import java.util.List;
import java.util.Random;

import static org.loioh.customenchantments.CustomEnchantments.EM;
import static org.loioh.customenchantments.CustomEnchantments.enchantments;


public class Enchant {
    private String id;
    private String name;
    private int lvl;
    private String itemsCategory;
    private String activateSlots;
    private String activateOn;
    private String cooldown = "-1";
    private List<String> commands = null;
    private List<String> attributes = null;
    private List<String> conflictWith = null;

    public Enchant(String id, String name, String itemsCategory, int lvl,String activateSlots,String activateOn,String cooldown,List<String>commands,List<String> attributes,List<String> conflictWith, Boolean add_bm) {
        this.id=id;
        this.name = name;
        this.lvl =lvl;
        this.itemsCategory = itemsCategory;
        this.activateSlots = activateSlots;
        this.activateOn = activateOn;
        this.cooldown = cooldown;
        this.commands = commands;
        this.attributes = attributes;
        this.conflictWith = conflictWith;
        if(add_bm) {
            EM.add(this);
        }
    }



    public static String[] path_i(String key){
        String[] path_g0 = {
                String.join(".", "Enchantments",key,"DisplayName"),
                String.join(".", "Enchantments",key,"MaxLevel"),
                String.join(".", "Enchantments",key,"ItemsCategory"),
                String.join(".", "Enchantments",key,"ActivationConditions.Slot"),
                String.join(".", "Enchantments",key,"ActivationConditions.Action"),
                String.join(".", "Enchantments",key,"ActivationConditions.Cooldown"),
                String.join(".", "Enchantments",key,"ActivationConditions.-dop1-"),
                String.join(".", "Enchantments",key,"ActivationConditions.-dop2-"),
                String.join(".", "Enchantments",key,"Activation.Attributes"),
                String.join(".", "Enchantments",key,"Activation.Commands"),
                String.join(".", "Enchantments",key,"ConflictWith")
        };
        return path_g0;
    }
    public static Enchant load_from_config(String key, Boolean save){
        String[] path = path_i(key);
        return load_from_config(path,key,save);
    }
    public static Enchant load_from_config(String path[], String key, Boolean save){
        String name  = (String) enchantments.get(path[0],true);
        int lvl = (int) enchantments.get(path[1],false);
        String items  = (String) enchantments.get(path[2],false);
        String activateSlot = (String) enchantments.get(path[3],false);
        String activateOn  = (String) enchantments.get(path[4],false);
        String cooldown = (String) enchantments.get(path[5],false);
        double dop1 = (double) enchantments.get(path[6],false);
        double dop2 = (double) enchantments.get(path[7],false);
        List<String> attributes = getListOrNull(enchantments,path[8],false);
        List<String> commands = getListOrNull(enchantments,path[9],false);
        List<String> conflictWith = getListOrNull(enchantments,path[10],false);

        return new Enchant(key,name,items,lvl,activateSlot,activateOn,cooldown,commands,attributes,conflictWith,save);
    }
    public static List<String> getListOrNull(Configs config, String path, boolean load){
        List<String> list = null;
        if(config.contains(path,load)) {
            list = (List<String>) config.getList(path, false);
        }
        return list;
    }

    public void save_to_config(String[] path){
        enchantments.set(path[0], getRawName());
        enchantments.set(path[1], getLevel());
        enchantments.set(path[2], getItemsCategory());
        enchantments.set(path[3], getActivateSlots());
        enchantments.set(path[4], getActivateOn());
        enchantments.set(path[5], getCooldown());
        //enchantments.set(path[6], );
        //enchantments.set(path[7], );
        enchantments.set(path[8], getAttributes());
        enchantments.set(path[9], getCommands());
        enchantments.set(path[10], getConflictWith());
        enchantments.save();
    }


    public void save_to_config(){
        String[] path = path_i(getId());
        save_to_config(path);
    }








    public boolean checkLevel( int lvl) {
        return lvl <= this.getLevel() && lvl > 0;
    }
    public boolean checkActivationChance(int lvl) {
        return true;
    }
















    //---SimpleGETTERS---
    public int getLevel() {
        return lvl;
    }
    public String getId() {
        return id;
    }

    public String getRawName() {
        return name;
    }
    public BaseComponent[] getName() {
        return _Init.list2(name);
    }
    public String getItemsCategory() {
        return itemsCategory;
    }
    public String getCooldown() {
        return cooldown;
    }

    public String getActivateOn() {
        return activateOn;
    }

    public String getActivateSlots() {
        return activateSlots;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public List<String> getConflictWith() {
        return conflictWith;
    }
}
