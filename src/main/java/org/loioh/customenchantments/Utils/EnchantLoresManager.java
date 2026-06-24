package org.loioh.customenchantments.Utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.loioh.customenchantments.CustomEnchantments;
import org.loioh.customenchantments.Enchantments.Enchant;
import org.loioh.customenchantments._Init;

import java.util.*;

import static org.loioh.customenchantments.Utils.ItemsManager.getOrSetRandom_item_enchantmentUUID;
import static org.loioh.customenchantments.Utils.ItemsManager.isItem;
import static org.loioh.customenchantments.Utils.Lores.space;

public class EnchantLoresManager {
    public static ItemStack addItemEnchantment(ItemStack item, Enchant enchant, int lvl){
        //if(!enchant.checkLevel(lvl)) return false;
        BaseComponent[] name = enchant.getName();
        BaseComponent[] new_name = new BaseComponent[name.length+2];
        String level = CustomEnchantments.lvls.getOrDefault(lvl+"",lvl+"");
        TextComponent lvl_text = new TextComponent();
        lvl_text.setText(level);
        lvl_text.setColor(name[0].getColor());
        lvl_text.setItalic(name[0].getStyle().isItalic());
        lvl_text.setBold(name[0].getStyle().isBold());

        for(int i = 0;i<name.length;i++) {
            new_name[i]=name[i];
        }

        new_name[new_name.length-2]=space();
        new_name[new_name.length-1]=lvl_text;
        item = Lores.addLore(item, new_name);
        return item;
    }

    public static List<String> getItemAllEnchantments(ItemStack item){
        List<String> minecraft =  getItemMinecraftEnchantmentsIds(item);
        if(minecraft == null) minecraft = new ArrayList<>();

        List<String> custom =  getItemEnchantmentsIds(item);
        if(custom  == null) custom  = new ArrayList<>();

        custom.addAll(minecraft);
        return custom;
    }

    public static List<String> getItemMinecraftEnchantmentsIds(ItemStack item){
        if(!item.hasItemMeta()) return null;
        ItemMeta im = item.getItemMeta();
        im.getEnchants();
        if(im.getEnchants()==null || im.getEnchants().isEmpty()) return null;
        List<String> list = new ArrayList<>();
        for( Enchantment en: im.getEnchants().keySet()){
            list.add(en.getName());
        }
        return list;
    }
    public static List<String> getItemEnchantmentsIds(ItemStack item){
        //List<String> customEnchPrefix = new ArrayList<>();
        //HashMap<String,Enchant> enchants = CustomEnchantments.GM.get_all();
        if(!item.hasItemMeta()) return null;
        HashMap<String,String> customEnchPrefix = CustomEnchantments.EM.get_prefixMapIds();
        List<BaseComponent[]> lores = Lores.getLoreComponents(item.getItemMeta(),false);
        List<String> ids = new ArrayList<>();
        if(lores!=null && !lores.isEmpty()) {
            for (BaseComponent[] bc : lores) {
                String parsed = _Init.list2(bc);
                //CustomEnchantments.msg("EnchantmentsLoreName: " + parsed);
                for(Map.Entry<String,String> entry:customEnchPrefix.entrySet()){
                    if(parsed.contains(entry.getValue())){
                        ids.add(entry.getKey());
                    }
                }
            }
        }

        return ids;
    }



    public static Multimap<Enchant, Map.Entry<Integer, UUID>> getPlayerActiveEnchantments(Player player){
        PlayerInventory inv = player.getInventory();
        Multimap<Enchant, Map.Entry<Integer, UUID>> enchants = ArrayListMultimap.create();

        //Armor
        for(ItemStack item : inv.getArmorContents()) {
            if (!isItem(item) || !item.hasItemMeta() || !item.getItemMeta().hasLore()) continue;
            HashMap<Enchant, String> customEnchPrefix = CustomEnchantments.EM.get_prefixMap("Armor");
            Map<Enchant, Integer> enchants0 = getItemEnchantments(item,customEnchPrefix);
            if(enchants0!=null && !enchants0.isEmpty()) {
                for (Map.Entry<Enchant, Integer> entry : enchants0.entrySet()) {
                    enchants.put(entry.getKey(), new AbstractMap.SimpleEntry<>(entry.getValue(), getOrSetRandom_item_enchantmentUUID(item,entry.getKey().getId())));
                }
            }
        }

        //Inventory
        for(ItemStack item : inv.getContents()) {
            if (!isItem(item) || !item.hasItemMeta() || !item.getItemMeta().hasLore()) continue;
            HashMap<Enchant, String> customEnchPrefix = CustomEnchantments.EM.get_prefixMap("Inv");
            Map<Enchant, Integer> enchants0 = getItemEnchantments(item,customEnchPrefix);
            if(enchants0!=null && !enchants0.isEmpty()) {
                for (Map.Entry<Enchant, Integer> entry : enchants0.entrySet()) {
                    enchants.put(entry.getKey(), new AbstractMap.SimpleEntry<>(entry.getValue(), getOrSetRandom_item_enchantmentUUID(item,entry.getKey().getId())));
                }
            }
        }

        ItemStack[] items = new ItemStack[]{inv.getItemInMainHand(),inv.getItemInOffHand()};
        boolean checkNext = true;
        for(ItemStack item : items) {
            if(!checkNext) continue;
            if (!isItem(item) || !item.hasItemMeta() || !item.getItemMeta().hasLore()) continue;

            checkNext = false;
            HashMap<Enchant, String> customEnchPrefix = CustomEnchantments.EM.get_prefixMap("Hand");
            Map<Enchant, Integer> enchants0 = getItemEnchantments(item, customEnchPrefix);
            if (enchants0 != null && !enchants0.isEmpty()) {
                for (Map.Entry<Enchant, Integer> entry : enchants0.entrySet()) {
                    enchants.put(entry.getKey(), new AbstractMap.SimpleEntry<>(entry.getValue(), getOrSetRandom_item_enchantmentUUID(item,entry.getKey().getId())));
                }
            }
        }

        return enchants;
    }

    public static HashMap<Enchant,Integer> getItemEnchantments(ItemStack item) {
        //List<String> customEnchPrefix = new ArrayList<>();
        //HashMap<String,Enchant> enchants = CustomEnchantments.GM.get_all();
        if(!item.hasItemMeta()) return null;
        HashMap<Enchant, String> customEnchPrefix = CustomEnchantments.EM.get_prefixMap();
        return getItemEnchantments(item,customEnchPrefix);
    }



    public static String textBSPart = "\"text\":\"";
    public static HashMap<Enchant,Integer> getItemEnchantments(ItemStack item,HashMap<Enchant,String> customEnchPrefix){
        if(!item.hasItemMeta()) return null;

        List<BaseComponent[]> lores = Lores.getLoreComponents(item.getItemMeta(),false);
        HashMap<Enchant,Integer> map = new HashMap<>();
        if(lores!=null && !lores.isEmpty()) {
            for (BaseComponent[] bc : lores) {
                String parsed = _Init.list2(bc);
                for(Map.Entry<Enchant,String> entry:customEnchPrefix.entrySet()){
                    if(parsed.contains(entry.getValue())){
                        parsed = parsed.replace(textBSPart+"\"","");
                        Enchant ench = entry.getKey();
                        int index = parsed.lastIndexOf(textBSPart);
                        if(index == -1){
                            map.put(ench,-1);
                            continue;
                        }
                        //CustomEnchantments.msg("EnchantmentsLoreName: " + parsed +" -> "+index+" -> "+parsed.substring(index + textBSPart.length()));

                        String lvl = parsed.substring(index + textBSPart.length()).split("\"")[0];
                        //CustomEnchantments.msg("EnchantmentsLevel["+ench.getId()+"][0]: " + lvl);
                        lvl = CustomEnchantments.reversedLvls.getOrDefault(lvl,lvl);
                        //CustomEnchantments.msg("EnchantmentsLevel["+ench.getId()+"][1]: " + lvl);
                        int level = -1;
                        try {
                            level = Integer.parseInt(lvl);
                        }catch (NumberFormatException ex){}

                        map.put(ench,level);
                    }
                }
            }
        }

        return map;
    }
}
