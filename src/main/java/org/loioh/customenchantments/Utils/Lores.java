package org.loioh.customenchantments.Utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.loioh.customenchantments.Utils.ItemsManager.isItem;
import static org.loioh.customenchantments._Init.list2;


public class Lores {

    //--- BASE --- Base ---
    public static List<BaseComponent[]> getLoreComponents(ItemMeta im,boolean check){
        if(!check || im.hasLore())return im.getLoreComponents();
        return null;
    }
    public static void setLoreComponents(ItemMeta im,List<BaseComponent[]> l){
        im.setLoreComponents(l);
    }
    public static List<String> getLore(ItemMeta im){
        if(im.hasLore())return im.getLore();
        return null;
    }
    public static ItemStack setLore(ItemStack i,List<String> l){
        ItemMeta im = i.getItemMeta();
        im.setLore(l);
        i.setItemMeta(im);
        return i;
    }


    public static BaseComponent[] getDisplayNameComponent(ItemMeta im,boolean check){
        if(!check || im.hasDisplayName()) return im.getDisplayNameComponent();
        return null;
    }
    public static void setDisplayNameComponent(ItemMeta im,BaseComponent[] n){
        im.setDisplayNameComponent(n);
    }
    public static String getDisplayName(ItemMeta im){
        if(im.hasDisplayName()) return im.getDisplayName();
        return null;
    }
    public static ItemStack setDisplayName(ItemStack i,String n){
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(n);
        i.setItemMeta(im);
        return i;
    }



    //--- HAS --- Has ---
    public static Boolean hasLore(ItemStack a,String text){
        if(isItem(a) && a.hasItemMeta() && a.getItemMeta().hasLore()){
            ItemMeta m = a.getItemMeta();
            List<BaseComponent[]> bl = getLoreComponents(m,false);
            List<String> l = m.getLore();
            if(bl != null && !bl.isEmpty()) {
                for (int i = 0; i < bl.size(); i++) {
                    BaseComponent[] ll = bl.get(i);
                    if(list2(ll).contains(text)){
                        return true;
                    }
                }
            }else if(l != null && !l.isEmpty()) {
                for (int i = 0; i < l.size(); i++) {
                    String ll = l.get(i);
                    if (ll.contains(text)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static Boolean hasName(ItemStack a,String text){
        if(isItem(a) && a.hasItemMeta() && a.getItemMeta().hasDisplayName()){
            ItemMeta m = a.getItemMeta();
            BaseComponent[] bn = getDisplayNameComponent(m,false);
            String n = m.getDisplayName();
            if(bn != null && bn.length >0) {
                if(list2(bn).contains(text)){
                    return true;
                }
            }else if(n != null) {
                if(n.contains(text)){
                    return true;
                }
            }
        }
        return false;
    }





    public static ItemStack replaceLore(ItemStack a,String lore0,String to){
        if(isItem(a) && a.hasItemMeta() && a.getItemMeta().hasLore()){
            ItemMeta m = a.getItemMeta();
            List<BaseComponent[]> bl = getLoreComponents(m,false);
            List<String> l = m.getLore();
            if(bl != null && !bl.isEmpty()) {
                for (int i = 0; i < bl.size(); i++) {
                    BaseComponent[] ll = bl.get(i);
                    ll = fix_bc(ll,lore0,to);
                    bl.set(i, ll);
                }
                setLoreComponents(m,bl);
            }else if(l != null && !l.isEmpty()) {
                for (int i = 0; i < l.size(); i++) {
                    String ll = l.get(i);
                    if (ll.contains(lore0)) {
                        ll = ll.replace(lore0, to);
                    }
                    l.set(i, ll);
                }
                m.setLore(l);
            }
            a.setItemMeta(m);
        }
        return a;
    }
    public static ItemStack replaceName(ItemStack a,String target,String to){
        if(isItem(a) && a.hasItemMeta() && a.getItemMeta().hasDisplayName()){
            ItemMeta m = a.getItemMeta();

            BaseComponent[] bn = getDisplayNameComponent(m,false);
            String n = m.getDisplayName();
            if(bn != null && bn.length >0) {
                bn = fix_bc(bn,target,to);
                setDisplayNameComponent(m,bn);
            }else if(n != null) {
                n = n.replace(target, to);
                m.setDisplayName(n);
            }

            a.setItemMeta(m);
        }
        return a;
    }














    public static ItemStack addLore(ItemStack a, String lore){
        ItemMeta m = a.getItemMeta();


        List<String> lores = m.hasLore() && m.getLore() != null ? m.getLore() : new ArrayList<>();

        lores.add(lore);
        m.setLore(lores);
        a.setItemMeta(m);
        return a;
    }
    public static ItemStack addLore(ItemStack a, BaseComponent[] lore){
        ItemMeta m = a.getItemMeta();

        List<BaseComponent[]> lores = Optional.ofNullable(getLoreComponents(m, true)).orElseGet(ArrayList::new);

        lores.add(lore);
        setLoreComponents(m,lores);
        a.setItemMeta(m);
        return a;
    }






    public static BaseComponent[] fix_bc(BaseComponent[] ll,String lore0,String to){
        for (int u = 0; u < ll.length; u++) {
            BaseComponent lll = ll[u];

            if(lll instanceof TextComponent){
                TextComponent l3 =(TextComponent)lll;
                String newT = l3.getText().replace(lore0,to);
                if(l3.getExtra()!=null) {
                    l3.setExtra(fix_bc(l3.getExtra(), lore0, to));
                }
                l3.setText(newT);
                ll[u] = l3;
            }
            if(lll instanceof  TranslatableComponent){
                TranslatableComponent l3 =(TranslatableComponent)lll;
                String newT = l3.getTranslate().replace(lore0,to);
                if(l3.getExtra()!=null) {
                    l3.setExtra(fix_bc(l3.getExtra(), lore0, to));
                }
                l3.setTranslate(newT);
                ll[u] = l3;
            }
        }
        return ll;
    }
    public static List<BaseComponent> fix_bc(List<BaseComponent> ll,String lore0,String to){
        for (int u = 0; u < ll.size(); u++) {
            BaseComponent lll = ll.get(u);

            if(lll instanceof TextComponent){
                TextComponent l3 =(TextComponent)lll;
                String newT = l3.getText().replace(lore0,to);
                if(l3.getExtra()!=null) {
                    l3.setExtra(fix_bc(l3.getExtra(), lore0, to));
                }
                l3.setText(newT);
                ll.set(u,l3);
            }
            if(lll instanceof  TranslatableComponent){
                TranslatableComponent l3 =(TranslatableComponent)lll;
                String newT = l3.getTranslate().replace(lore0,to);
                if(l3.getExtra()!=null) {
                    l3.setExtra(fix_bc(l3.getExtra(), lore0, to));
                }
                l3.setTranslate(newT);
                ll.set(u,l3);
            }
        }
        return ll;
    }

    public static TextComponent space(){
        TextComponent space = new TextComponent();
        space.setText(" ");
        space.setItalic(false);
        space.setBold(false);
        return space;
    }
}
