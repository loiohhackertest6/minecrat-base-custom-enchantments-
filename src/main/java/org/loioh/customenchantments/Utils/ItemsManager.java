package org.loioh.customenchantments.Utils;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.loioh.customenchantments.CustomEnchantments;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ItemsManager {

    public static ItemStack set_item_data(ItemStack item, String key, String value) {
        if (isItem(item)){
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.getPersistentDataContainer().set(new NamespacedKey(CustomEnchantments.getInstance(), key), PersistentDataType.STRING, value.toString());
            item.setItemMeta(meta);
            return item;
        }
        return null;
    }
    public static String get_item_data(ItemStack i_item, String key) {
        if (isItem(i_item) && i_item.hasItemMeta()) {
            if(i_item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(CustomEnchantments.getInstance(), key),PersistentDataType.STRING)) {
                String id = i_item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(CustomEnchantments.getInstance(), key), PersistentDataType.STRING);
                return id;
            }
        }
        return null;
    }
    public static String getOrSet_item_data(ItemStack i_item, String key,String def) {
        if (isItem(i_item)) {
            if(i_item.hasItemMeta() && i_item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(CustomEnchantments.getInstance(), key),PersistentDataType.STRING)) {
                String id = i_item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(CustomEnchantments.getInstance(), key), PersistentDataType.STRING);
                return id;
            }
            set_item_data(i_item,key,def);
            return def;
        }
        return null;
    }
    public static ItemStack set_item_id(ItemStack item, String custom_id) {
        return set_item_data(item, "custom_id",custom_id);

    }
    public static String get_item_id(ItemStack item) {
        return get_item_data(item,"custom_id");
    }

    public static ItemStack set_item_enchantmentUUID(ItemStack item,String enchant_id, UUID uuid) {
        return set_item_data(item, "enchantment_uuid."+enchant_id,uuid.toString());
    }
    public static UUID get_item_enchantmentUUID(ItemStack item,String enchant_id) {
        String str = get_item_data(item,"enchantment_uuid."+enchant_id);if(str!=null){
            try {
                return UUID.fromString(str);
            } catch (Exception e) {}
        }
        return null;
    }


    public static UUID getOrSetRandom_item_enchantmentUUID(ItemStack item,String enchant_id) {
        String str = getOrSet_item_data(item,"enchantment_uuid."+enchant_id,UUID.randomUUID().toString());
        if(str!=null){
            try {
                return UUID.fromString(str);
            } catch (Exception e) {}
        }
        return null;
    }


    
    /*






    // */

    public static Boolean remove_pdc(ItemStack i_item,String... keys) {
        if(keys==null || keys.length<=0){
            return false;
        }
        if (!isItem(i_item) || !i_item.hasItemMeta() || i_item.getItemMeta().getPersistentDataContainer()==null || i_item.getItemMeta().getPersistentDataContainer().isEmpty()) {
            return false;
        }
        for(String key:keys){
            i_item.getItemMeta().getPersistentDataContainer().remove(new NamespacedKey(CustomEnchantments.getInstance(), key));
        }
        return true;
    }










    public static final String site = "https://www.fiverr.com/s/3j4g7m";

    public static ItemStack enchant_i(ItemStack i, Enchantment e, int lvl, Boolean hide) {
        if (isItem(i)) {
            ItemStack item = i.clone();
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.addEnchant(e, (lvl+1), false);
            if (hide) {
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            item.setItemMeta(itemMeta);
            return item;
        }
        return null;
    }
    public static ItemStack unbreakable_i(ItemStack i, Boolean u) {
        if(isItem(i)) {
            ItemStack item = i.clone();
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setUnbreakable(u);
            item.setItemMeta(itemMeta);
            return item;
        }
        return null;
    }
    public static ItemStack attribute_i(ItemStack i, Attribute attribute, String name, double value, AttributeModifier.Operation operation, EquipmentSlot... slots) {
        if(isItem(i)) {
            ItemStack item = i.clone();
            ItemMeta itemMeta = item.getItemMeta();
            for (EquipmentSlot e : slots) {
                AttributeModifier aas = new AttributeModifier(UUID.randomUUID(), name, value, operation, e);
                itemMeta.addAttributeModifier(attribute, aas);
            }
            item.setItemMeta(itemMeta);
            return item;
        }
        return null;
    }












    public static boolean hasItemInInventory(Player player, Material type) {
        if (player.getInventory().getItemInMainHand().getType() == type ||
                player.getInventory().getItemInOffHand().getType() == type) {
            return true;
        }
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasItemInHands(Player player, Material type) {
        return player.getInventory().getItemInMainHand().getType() == type ||
                player.getInventory().getItemInOffHand().getType() == type;
    }

    public static boolean hasItemInArmor(Player player, Material type) {
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null && item.getType() == type) {
                return true;
            }
        }
        return false;
    }












    public static Boolean isItem(ItemStack a){
        if( a != null && a.getType() != null && a.getType() != Material.AIR){
            return true;
        }
        return false;
    }

    public static ItemStack remove(ItemStack a,int amount){
        if(a != null) {
            if (a.getAmount() > amount) {
                a.setAmount(a.getAmount() - amount);
            } else {
                a.setAmount(0);
                a = null;
            }
        }
        return a;
    }


    private static boolean isVerySimilar(ItemStack a, ItemStack b) {
        if (!isItem(a) || !isItem(b)) return false;
        if (a.getType() != b.getType()) return false;

        ItemMeta metaA = a.getItemMeta();
        ItemMeta metaB = b.getItemMeta();

        if (metaA == null && metaB == null) return true;
        if (metaA == null || metaB == null) return false;

        if (metaA.hasCustomModelData() != metaB.hasCustomModelData()) return false;
        if (metaA.hasCustomModelData() && metaA.getCustomModelData() != metaB.getCustomModelData()) return false;

        return true;
    }
    public static int Count(Player p, ItemStack item) {
        Boolean mode = true;
        if (!isItem(item)) {
            mode = false;
        }else {
            item = item.clone();
            item.setAmount(1);
        }

        Inventory inv = p.getInventory();
        int amount = 0;
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack current = inv.getItem(i);

            if(mode) {
                if (isItem(current) && isVerySimilar(current, item)) {
                    amount += current.getAmount();
                }
            }else if(!isItem(current)) {//empty slots
                amount += 1;
            }
        }

        return amount;
    }
    public static boolean tryClear(Player p, ItemStack item, int amount) {
        if (item == null || amount <= 0) return false;


        item = item.clone();
        item.setAmount(1);

        Inventory inv = p.getInventory();
        int remaining = amount;
        List<Integer> matchingSlots = new ArrayList<>();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack current = inv.getItem(i);

            if (isItem(current) && isVerySimilar(current, item)) {
                remaining -= current.getAmount();
                matchingSlots.add(i);
                //if (remaining <= 0) break;
            }
        }
        if (remaining > 0) {
            return false;
        }
        remaining = amount;
        for (int slot : matchingSlots) {
            ItemStack current = inv.getItem(slot);
            if (current == null) continue;

            int currentAmount = current.getAmount();
            if (currentAmount <= remaining) {
                inv.setItem(slot, null);
                remaining -= currentAmount;
            } else {
                current.setAmount(currentAmount - remaining);
                inv.setItem(slot, current);
                break;
            }
        }

        return true;
    }

    public static void giveItems(Player player, ItemStack item, int amount) {
        if (item == null || amount <= 0) return;

        item = item.clone();
        item.setAmount(1);

        Inventory inv = player.getInventory();
        int maxStackSize = item.getMaxStackSize();

        int remaining = amount;

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack current = inv.getItem(i);

            if (isItem(current) && isVerySimilar(current, item)) {
                int currentAmount = current.getAmount();
                int space = maxStackSize - currentAmount;

                if (space > 0) {
                    int toAdd = Math.min(space, remaining);
                    current.setAmount(currentAmount + toAdd);
                    inv.setItem(i, current);
                    remaining -= toAdd;

                    if (remaining <= 0) return;
                }
            }
        }

        for (int i = 0; i < inv.getSize(); i++) {
            if (!isItem(inv.getItem(i))) {
                int toAdd = Math.min(maxStackSize, remaining);
                ItemStack newItem = item.clone();
                newItem.setAmount(toAdd);
                inv.setItem(i, newItem);
                remaining -= toAdd;

                if (remaining <= 0) return;
            }
        }

        while (remaining > 0) {
            int toDrop = Math.min(maxStackSize, remaining);
            ItemStack dropItem = item.clone();
            dropItem.setAmount(toDrop);
            player.getWorld().dropItemNaturally(player.getLocation(), dropItem);
            remaining -= toDrop;
        }
    }






    public static Material getMaterial(String material){
        material = material.toUpperCase();
        Material mat = null;
        try{
            mat=Material.valueOf(material);
        }catch (Exception ex){
            try{
                mat=Material.getMaterial(material);
            }catch (Exception ex2){

            }
        }
        return mat;
    }

    public static List<Material> getMaterials(List<String> materials){
        List<Material> mats = new ArrayList<>();
        for(String material:materials){
            Material mat = getMaterial(material);
            if(mat!=null){
                mats.add(mat);
            }
        }
        if(mats.isEmpty()) mats = null;
        return mats;
    }

    public static boolean isMatInList(Material mat,List<String> materials){
        String material0 = mat.name();
        for(String material:materials){
            if(material.contains("*")){
                //"*_head"
                int star = material.indexOf('*');
                if (star >= 0) {
                    String prefix = material.substring(0, star);
                    String suffix = material.substring(star + 1);

                    if ((prefix.isEmpty() || material0.startsWith(prefix)) &&
                            (suffix.isEmpty() || material0.endsWith(suffix))) {
                        return true;
                    }
                } else {
                    if (material0.equals(material)) {
                        return true;
                    }
                }
            }else{
                if(material0.equals(material)){
                    return true;
                }
            }
        }
        return false;
    }


}
