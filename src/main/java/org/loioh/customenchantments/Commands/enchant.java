package org.loioh.customenchantments.Commands;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.loioh.customenchantments.CustomEnchantments;
import org.loioh.customenchantments.Enchantments.Enchant;
import org.loioh.customenchantments.Utils.EnchantLoresManager;
import org.loioh.customenchantments.Utils.ItemsManager;
import org.loioh.customenchantments.Utils.Lores;
import org.loioh.customenchantments.Utils.Messages;
import org.loioh.customenchantments._Init;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.loioh.customenchantments.CustomEnchantments.msg;
import static org.loioh.customenchantments.Utils.EnchantLoresManager.getItemAllEnchantments;
import static org.loioh.customenchantments.Utils.ItemsManager.isItem;
import static org.loioh.customenchantments.Utils.TimeConvert.getNumber;
import static org.loioh.customenchantments.Utils.TimeConvert.safeDoubleToInt;
import static org.loioh.customenchantments._Init.hashMap;
import static org.loioh.customenchantments._Init.list;


public class enchant implements CommandExecutor, TabCompleter {
    public static final String author="loioh";

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            Messages.sendMessage(sender,"commands.not_player.denied");
            return true;
        }
        Player player =(Player)sender;

        if(command.getName().equalsIgnoreCase("cenchant") || command.getName().equalsIgnoreCase("custom_enchant") || command.getName().equalsIgnoreCase("ce")){
            if ((sender).isOp() || ((Player) sender).hasPermission("customenchantments.commands.enchant")) {
                if(args.length<1){
                    Messages.sendMessage(sender,"commands.args_amount.denied");
                    return true;
                }
                Enchant enchant = CustomEnchantments.EM.find_id(args[0]);
                int lvl = 1;
                if(args.length>=2) {
                     lvl = safeDoubleToInt(getNumber(args[1]));
                }
                if(enchant!=null) {
                    addEnchantment(sender, player, enchant, lvl);
                }else{
                    Messages.sendMessage(sender,"commands.enchant.wrong_enchantment_id.denied",hashMap("arg",args[0]));
                }
            }else{
                Messages.sendMessage(sender,"commands.permission.denied");
            }
            return  true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if(args.length == 1){
            list.addAll(CustomEnchantments.EM.get_ids());
        }
        if(args.length == 2){
            Enchant enchant = CustomEnchantments.EM.find_id(args[0]);
            if(enchant!=null) {
                list.addAll(list("1",enchant.getLevel()+""));
            }
        }
        return list;
    }





    public static void addEnchantment(CommandSender sender, Player player, Enchant enchant, int lvl){
        if(!enchant.checkLevel(lvl)){
            Messages.sendMessage(sender,"commands.enchant.wrong_level.denied");
            return;
        }
        boolean main_hand = true;
        ItemStack item = player.getInventory().getItemInMainHand();
        if(!isItem(item)) {
            item = player.getInventory().getItemInOffHand();
            main_hand=false;
        }
        if(!isItem(item)) {
            Messages.sendMessage(sender, "commands.enchant.not_item_in_hand.denied");
            return;
        }


        String category = enchant.getItemsCategory();
        boolean allow = false;
        if(category==null || !category.toLowerCase().startsWith("all")){
            List<String> items = (List<String>)CustomEnchantments.lists.getList("ItemsCategory."+category,true);
            if(items!=null && !items.isEmpty() && ItemsManager.isMatInList(item.getType(),items)){
                allow = true;
            }
        }else {
            allow = item.getType().isItem();
        }
        if(!allow){
            Messages.sendMessage(sender, "commands.enchant.wrong_item_category.denied");
            return;
        }


        List<String> currentList = getItemAllEnchantments(item);
        if(enchant.getConflictWith()!=null){
            for (String conflict : enchant.getConflictWith()) {
                if(currentList.contains(conflict)){
                    Messages.sendMessage(sender, "commands.enchant.conflict.denied");
                    return;
                }
            }
        }
        //conflicts
        if(currentList.contains(enchant.getId())){
            msg("already  enchanted, clearing...");
            removeEnchantment(item,enchant);
        }


        item = EnchantLoresManager.addItemEnchantment(item,enchant,lvl);
        tryApplyEnchantmentGlint(item);

        player.getInventory().setItem(main_hand? EquipmentSlot.HAND:EquipmentSlot.OFF_HAND,item);
        Messages.sendMessage(sender, "commands.enchant.success");
    }
    public static void tryApplyEnchantmentGlint(ItemStack item){
        ItemMeta im = item.getItemMeta();
        if(!im.hasEnchantmentGlintOverride() || im.getEnchantmentGlintOverride()!=null) {
            im.setEnchantmentGlintOverride(true);
        }
        item.setItemMeta(im);
    }

    public static void removeEnchantment(ItemStack item, Enchant enchant){
        ItemMeta im = item.getItemMeta();
        List<BaseComponent[]> lores = Lores.getLoreComponents(im,false);
        List<BaseComponent[]> new_lores = new ArrayList<>();
        if(lores!=null && !lores.isEmpty()) {
            for (BaseComponent[] bc : lores) {
                String parsed = _Init.list2(bc);
                if(!parsed.contains(enchant.getRawName())){
                    new_lores.add(bc);
                }
            }
        }
        Lores.setLoreComponents(im,new_lores);
        item.setItemMeta(im);
    }
}
