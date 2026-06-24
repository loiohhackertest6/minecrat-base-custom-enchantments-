package org.loioh.customenchantments.Enchantments;

import org.bukkit.plugin.java.JavaPlugin;
import org.loioh.customenchantments.CustomEnchantments;

import java.util.*;

public class EnchantmentsManager {


    static JavaPlugin plugin;
    private static List<? super Enchant> items;

    public EnchantmentsManager(JavaPlugin plugin) {
        this.plugin = plugin;
        items = new ArrayList<>();
    }

    public void add(Enchant item) {
        if(get_ids().contains(item.getId())){
            items.remove(this.find_id(item.getId()));
        }
        items.add(item);
        item.save_to_config();
    }

    public void remove(Enchant item) {
        if (items.contains(item)) {
            items.remove(item);
        }
    }
    public void remove(String id) {
        if(get_ids().contains(id)){
            items.remove(this.find_id(id));
        }
    }

    public Enchant find_id(String id) {
        if (id == null) {
            return null;
        }
        for (Object i : items) {
            if (i instanceof Enchant) {
                Enchant i0 = (Enchant) i;
                if (i0.getId().equals(id)) {
                    return i0;
                }
            }
        }
        return null;
    }

    public List<String> get_ids() {
        List<String> ids = new ArrayList<>();
        for (Object i : items) {
            Enchant i0 = (Enchant) i;
            ids.add(i0.getId());
        }
        return ids;
    }

    public Enchant find_name(String name) {

        if (name != null) {
            for (Object i : items) {
                if (i instanceof Enchant) {
                    Enchant i0 = (Enchant) i;
                    if (i0.getName().equals(name)) {
                        return i0;
                    }
                }
            }
        }
        return null;
    }

    public HashMap<String, Enchant> get_all() {

        HashMap<String, Enchant> ids = new HashMap<>();
        for (Object i : items) {
            Enchant i0 = (Enchant) i;
            ids.put(i0.getId(),i0);
        }
        return ids;
    }


    public HashMap<Enchant, String> get_prefixMap(String slot) {

        HashMap<Enchant, String> ids = new HashMap<>();
        for (Object i : items) {
            Enchant i0 = (Enchant) i;
            if(i0.getActivateSlots().startsWith(slot)) {
                ids.put(i0, i0.getRawName());
            }
        }
        return ids;
    }
    public HashMap<Enchant, String> get_prefixMap() {

        HashMap<Enchant, String> ids = new HashMap<>();
        for (Object i : items) {
            Enchant i0 = (Enchant) i;
            CustomEnchantments.msg("EnchantmentsRawName: "+ i0.getRawName());
            ids.put(i0,i0.getRawName());
        }
        return ids;
    }
    public HashMap<String, String> get_prefixMapIds() {

        HashMap<String, String> ids = new HashMap<>();
        for (Object i : items) {
            Enchant i0 = (Enchant) i;
            CustomEnchantments.msg("EnchantmentsRawName: "+ i0.getRawName());
            ids.put(i0.getId(),i0.getRawName());
        }
        return ids;
    }
}
