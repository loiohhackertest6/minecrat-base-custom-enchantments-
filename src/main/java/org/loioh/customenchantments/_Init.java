package org.loioh.customenchantments;

import com.google.gson.JsonSyntaxException;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Material;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.loioh.customenchantments.Enchantments.Enchant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;




public class _Init {

    public static void init(JavaPlugin plugin){
        //CustomEnchantments.EconomySystem = Meltoins.init(plugin);

        init_S(plugin);
        init_L(plugin);
        init_E(plugin);
    }
    public static void init_S(JavaPlugin plugin){
        CustomEnchantments.config = new Configs(plugin,"config");

        //CustomEnchantments.baseTimer = (String) _Init.gos(CustomEnchantments.config, "StartTimerHours", CustomEnchantments.baseTimer);

        CustomEnchantments.messages = (HashMap<String,String>) _Init.gosH(CustomEnchantments.config, "Messages", (HashMap<String,Object>)CustomEnchantments.messages.clone()).clone();
        CustomEnchantments.lvls = (HashMap<String,String>) _Init.gosH(CustomEnchantments.config, "EnchantmentLevelSymbols", (HashMap<String,Object>)CustomEnchantments.lvls.clone()).clone();


        CustomEnchantments.Debug = (Boolean) _Init.gos( CustomEnchantments.config, "DebugLog",  CustomEnchantments.Debug);

        CustomEnchantments.lvls.forEach((key, value) ->
                CustomEnchantments.reversedLvls.put(value, key)
        );
    }
    public static void init_L(JavaPlugin plugin){
        CustomEnchantments.lists = new Configs(plugin,"lists");

        _Init.gos( CustomEnchantments.lists, "Effects.ListForEffect2", list(
                PotionEffectType.SPEED.getName()+":1",
                    PotionEffectType.HASTE.getName()+":1"
        ));


        _Init.gos( CustomEnchantments.lists, "ItemsCategory.Shields", list(Material.SHIELD.name()));
        _Init.gos( CustomEnchantments.lists, "ItemsCategory.Armors", list("*_BOOTS","*_LEGGINGS","*_HELMET","*_CHESTPLATE","*_HEAD",Material.SKELETON_SKULL.name()));
    }
    public static void init_E(JavaPlugin plugin){
        CustomEnchantments.enchantments = new Configs(plugin,"enchantments");

        Set<String> g =  CustomEnchantments.enchantments.getCS("Enchantments");
        if (g != null && !g.isEmpty()) {
            for(String key:g){
                Enchant.load_from_config(key,true);
            }
        }else{
            // Shields
            new Enchant("effect1", list2(new BaseComponent[]{sn("Effect1Shield", ChatColor.GRAY)}),"Shields",3,"Hands","onShieldCooldownStart","5s",null,list(Attribute.ATTACK_DAMAGE.name()+":"+AttributeModifier.Operation.ADD_SCALAR.name()+":0.1*%level%:7",Attribute.ATTACK_SPEED.name()+":"+AttributeModifier.Operation.ADD_SCALAR.name()+":0.1*%level%:7"),null, true);

            //Armor
            new Enchant("effect2", list2(new BaseComponent[]{sn("Effect2Armor", ChatColor.GRAY)}),"Armors",3,"Armors","onGetDamage","5s",list("effectfromlist %player% ListForEffect2 10"),null,null, true);

        }


        CustomEnchantments.enchantments.save();
    }

    
    
    
    
    



    public static HashMap<String,String> bM(){
        HashMap<String,String> h = new HashMap<>();
        //Commands
        h.put("commands,permission,denied", "&cYou haven't permission for use the command");
        h.put("commands,not_player,denied", "&cThe command can be processed with console");
        h.put("commands,not_recognized_number,denied", "&cThe command can be processed the number %arg% not recognized");
        h.put("commands,not_recognized_player,denied", "&cThe player %arg% not founded or offline");
        h.put("commands,not_recognized_path,denied", "&cThe list or path %arg% not founded or list incorrect");
        h.put("commands,args_amount,denied", "&cArguments amount not matched");

        //reload
        h.put("commands,reload,success", org.bukkit.ChatColor.GREEN+"Reloaded");

        //enchant
        h.put("commands,enchant,wrong_enchantment_id,denied","&cThe enchantment not recognized check the id %arg%");
        h.put("commands,enchant,wrong_level,denied","&cThe enchantment not support the lvl");
        h.put("commands,enchant,not_item_in_hand,denied","&cYou must have an item in hand to enchant it");
        h.put("commands,enchant,wrong_item_category,denied","&cThe enchantment not support the items");
        h.put("commands,enchant,conflict,denied","&cThe enchantment cannot applied to items with conflicting enchantments");
        h.put("commands,enchant,success", org.bukkit.ChatColor.GREEN+"Enchanted");


        return h;
    }

    public static String[] ones = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
    public static String[] tens = {"", "X", "XX", "XXX", "XL", "L"};

    public static HashMap<String, String> lvlRoman() {
        HashMap<String, String> h = new HashMap<>();
        for (int i = 1; i <= 50; i++) {
            int ten = i / 10;
            int one = i % 10;
            h.put(String.valueOf(i), tens[ten] + ones[one]);
        }

        return h;
    }
    public static String toRoman(int num) {
        if (num < 1 || num > 50) return String.valueOf(num);
        return tens[num / 10] + ones[num % 10];
    }


















    public static String space(int amount){
        if(amount <0){
            amount=0;
        }
        String t = "";
        for(int i=0; i<amount; i++){
            t+=" ";
        }
        return t;
    }



    public static List<String> list(String... a) {
        List<String> l = new ArrayList<>();
        for (String i : a) {
            l.add(i);
        }
        return l;
    }
    public static List<String> list(List<PotionEffectType> a) {
        List<String> l = new ArrayList<>();
        for (PotionEffectType i : a) {
            l.add(i.getName().toUpperCase());
        }
        return l;
    }
    public static List<Integer> list(Integer... a) {
        List<Integer> l = new ArrayList<>();
        for (Integer i : a) {
            l.add(i);
        }
        return l;
    }
    public static List<Material> list(Material... a) {
        List<Material> l = new ArrayList<>();
        for (Material i : a) {
            l.add(i);
        }
        return l;
    }
    public static List<ItemStack> list(ItemStack... a) {
        List<ItemStack> l = new ArrayList<>();
        for (ItemStack i : a) {
            l.add(i);
        }
        return l;
    }
    public static List<BaseComponent[]> list(BaseComponent[]... a){
        List<BaseComponent[]> l = new ArrayList<>();
        for(BaseComponent[] b:a){
            l.add(b);
        }
        return l;
    }

    public static String list2(BaseComponent[] a){
        String s = ComponentSerializer.toString(a);
        return s;
    }
    public static BaseComponent[] list2(String a){
        return tryParse(a);
    }
    public static BaseComponent[] tryParse(String a) {
        try {
            return ComponentSerializer.parse(a);
        } catch (JsonSyntaxException ex) {
            return null;
        }
    }

    public static TranslatableComponent tn(String name){
        TranslatableComponent textC =new TranslatableComponent(name) ;
        textC.setColor(net.md_5.bungee.api.ChatColor.WHITE);
        textC.setItalic(false);
        return textC;
    }
    public static TextComponent sn(String name){
        TextComponent textC =new TextComponent(name);
        textC.setItalic(false);
        return textC;
    }
    public static TextComponent sn(String name, ChatColor color) {
        TextComponent textC = new TextComponent(name);
        textC.setItalic(false);
        textC.setColor(color);
        return textC;
    }

    public static TranslatableComponent tn(String name, net.md_5.bungee.api.ChatColor color){
        TranslatableComponent textC =new TranslatableComponent(name) ;
        textC.setColor(color);
        textC.setItalic(false);
        return textC;
    }
    public static TextComponent hl_text(String text, String hover_text, String link) {
        HashMap<HoverEvent.Action,String> HE = new HashMap<>();
        HashMap<ClickEvent.Action,String> CE = new HashMap<>();
        HE.put(HoverEvent.Action.SHOW_TEXT, hover_text);
        CE.put(ClickEvent.Action.OPEN_URL, link);
        return textComponent(text,HE,CE);
    }
    public static TextComponent textComponent(String text, HashMap<HoverEvent.Action,String> hover_events, HashMap<ClickEvent.Action,String> click_events) {
        TextComponent text1 = new TextComponent(text);
        if(hover_events!=null && !hover_events.isEmpty()) {
            for(HoverEvent.Action action : hover_events.keySet()) {
                String result = hover_events.get(action);
                if(action == HoverEvent.Action.SHOW_TEXT) {
                    text1.setHoverEvent(new HoverEvent(action, convertText(result,Text.class) ));
                }else  if(action == HoverEvent.Action.SHOW_ACHIEVEMENT) {
                    text1.setHoverEvent(new HoverEvent(action, convertText(result,Text.class) ));
                }
            }
        }

        if(click_events!=null && !click_events.isEmpty()) {
            for(ClickEvent.Action action : click_events.keySet()) {
                String value = click_events.get(action);
                text1.setClickEvent(new ClickEvent(action, value));
            }
        }
        return text1;
    }

    public static Content convertText(String text){ return convertText(text,Text.class);}
    public static Content convertText(String text,Class target){
        Content content = null;
        if(target == Text.class) {
            BaseComponent[] bc = org.loioh.customenchantments._Init.list2(text);
            if (bc != null){
                content = new Text(bc);
            }else {
                content = new Text(text);
            }
        }
        return content;
    }


    public static HashMap<String,Object> gosH(Configs config, String path0, HashMap<String,Object> l) {
        if (!config.contains(path0,false)) {
            config.set(path0, l);
            config.save();
            return l;
        } else {
            return config.getHashMap(path0,false);
        }
    }

    public static List<Object> gos(Configs config, String path0, List<Object> l) {
        if (!config.contains(path0,false)) {
            config.set(path0, l);
            config.saveConfig();
            return l;
        } else {
            return (List<Object>) config.getList(path0,false);
        }
    }
    public static Object gos(Configs config,String path0,Object o){
        if(!config.contains(path0,false)) {
            config.set(path0,o);
            config.saveConfig();
            return o;
        }else{
            return config.get(path0,false);
        }
    }

    public static HashMap<String, String> hashMap(String... a) {
        if(a!=null && a.length> 0 && a.length % 2 == 0){
            HashMap<String,String> h = new HashMap<>();
            for(int i =0;i< a.length;i+=2){
                h.put(a[i],a[i+1]);
            }
            return h;
        }
        return null;
    }
}
