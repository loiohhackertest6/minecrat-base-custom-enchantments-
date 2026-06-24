package org.loioh.customenchantments.Utils;


import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.loioh.customenchantments.CustomEnchantments;
import org.loioh.customenchantments.Enchantments.Enchant;

import java.util.*;

import static org.loioh.customenchantments.CustomEnchantments.msg;
import static org.loioh.customenchantments.Utils.Messages.fixPlaceholders;
import static org.loioh.customenchantments.Utils.TimeConvert.*;
import static org.loioh.customenchantments._Init.hashMap;

public class Effects {


    public static void breachPlayer(Player target,double percent){
        breachPlayer(target,percent,5);
    }
    public static void breachPlayer(Player target,double percent,int ticks) {
        //msg("breachPlayer: "+target.getName());
        AttributeInstance attr = target.getAttribute(Attribute.ARMOR);
        if (attr == null) return;

        //msg("Armor modifier: creating");
        UUID modifierUUID = UUID.randomUUID();
        attr.addModifier(new AttributeModifier(modifierUUID , modifierUUID+"", -(percent),AttributeModifier.Operation.MULTIPLY_SCALAR_1));


        //msg("AttrAdded -> "+attr.getAttribute().name());
        Bukkit.getScheduler().runTaskLater(CustomEnchantments.getInstance(), () -> {
            attr.removeModifier(modifierUUID);
            msg("AttrRemoved -> "+attr.getAttribute().name());
        }, ticks);
    }


    public static void heal(Player player, double hp) {
        hp = player.getHealth()+hp;
        hp = hp>=player.getMaxHealth() ? player.getMaxHealth() : hp;
        player.setHealth(hp);
    }

    public static void setGoldenHearts(Player player, double hp) {

        AttributeInstance maxAbsorption =
                player.getAttribute(Attribute.MAX_ABSORPTION);

        if (maxAbsorption != null) {
            maxAbsorption.setBaseValue(hp);
        }
        player.setAbsorptionAmount(hp);
    }

























    //PotionEffects
    public static Object[] getEffect(String effect){
        effect = effect.toUpperCase();
        int lvl = 1;
        if(effect.contains(":")){
            int separator = effect.indexOf(':');
            if (separator > 0) {
                String ef2 = effect.substring(0, separator);
                try{
                    lvl = Integer.parseInt(effect.substring(separator + 1));
                } catch (NumberFormatException e) {}
                effect = ef2;
            }

        }
        PotionEffectType ef = null;
        try{
            ef = PotionEffectType.getByName(effect);
        }catch (Exception ex){
            try{
                ef= PotionEffectType.getByKey(NamespacedKey.fromString(effect));
            }catch (Exception ex2){

            }
        }
        return new Object[]{ef,lvl};
    }

    public static List<Object[]> getEffects(List<String> effects){
        List<Object[]> mats = new ArrayList<>();
        for(String effect:effects){
            Object[] obj = getEffect(effect);
            if(obj!=null && obj[0]!=null){
                mats.add(obj);
            }
        }
        if(mats.isEmpty()) mats = null;
        return mats;
    }


    public static Object[] getRandomEffectL(List<String> effects){
        List<Object[]> efs = getEffects(effects);
        return getRandomEffect(efs);
    }

    public static Object[] getRandomEffect(List<Object[]> efs){
        double rd = new Random().nextDouble();
        int ri = (int)(rd * efs.size());
        return efs.get(ri);
    }

    public static PotionEffect getEffect(Object[] obj,int durationS){
        if(obj==null || obj.length<=0) return null;
        PotionEffectType pet= (PotionEffectType) obj[0];
        int lvl = (int) obj[1];
        return new PotionEffect(pet,durationS*20,lvl-1,true,false,true);
    }
    //ListForDivine
    public static boolean applyRandomEffectFromList(Player player, String list, int durationS){
        String path = "Effects."+list;
        List<String> effects = null;
        if(CustomEnchantments.lists.contains(path,true)) {
            effects = (List<String>) CustomEnchantments.lists.getList(path, false);
        }
        msg("EffectsList: "+effects.size());
        if(effects==null) return false;


        Object[] ef = getRandomEffectL(effects);
        PotionEffect pef = getEffect(ef, durationS);
        msg("EffectsListGet: "+ef+" -> "+pef);
        if(pef!=null) {
            player.addPotionEffect(pef);
            return true;
        }
        return false;
    }


















    public static void runCommands(List<String> commands, HashMap<String,String> placeH){
        if(commands!=null && !commands.isEmpty()) {
            for (String c : commands) {
                c = fixPlaceholders(c,placeH);

                msg("CommandsProcess: "+c);
                CommandSender s = Bukkit.getConsoleSender();
                boolean completed = Bukkit.dispatchCommand(s, c);
                if (!completed) {
                    msg("this command didn't work correctly ask op for help");
                    break;
                }
            }
        }
    }

    public static void runEnchantAbilities(Player player,UUID uuid,Enchant enchant,int lvl){
        msg("preAbilityCheck: "+enchant.getId()+" : "+lvl);
        if(enchant.checkActivationChance(lvl)) {
            msg("abilityChanceCheckComplited");
            if (isAbilityAvailable(player,uuid, enchant, lvl)) {
                msg("abilityActivation");
                runAttributes(player, enchant, lvl);
                runCommands(player, enchant, lvl);
            }
        }
    }

    public static void runCommands(Player player, Enchant enchant, int lvl){
        HashMap<String,String> placeH = hashMap(
                "player",player.getName(),
                "as_player","execute as " + player.getName() + " at @s run",
                "lvl",lvl+"","level",lvl+""
        );
        runCommands(enchant.getCommands(),placeH);

    }
    public static HashMap<UUID, Set<String>> attrModifier = new HashMap<>();


    public static void runAttributes(Player player,Enchant enchant,int lvl){
        List<String> attributes = enchant.getAttributes();
        if(attributes!=null && !attributes.isEmpty()) {
            Set<String> data = new HashSet<>();
            HashMap<String,String> placeH = hashMap(
                    "player",player.getName(),
                    "lvl",lvl+"","level",lvl+""
            );
            for (String c : attributes) {
                if(c.contains(":")) {
                    c = fixPlaceholders(c,placeH);
                    String[] data0 = c.split(":");
                    if(data0.length >= 3){
                        for(String d0 : data0){
                            msg("Vx -> "+d0);
                        }
                    }
                    try {
                        AttributeInstance attr = player.getAttribute(Attribute.valueOf(data0[0]));
                        Double amount = calculateSafe(data0[2]);
                        AttributeModifier.Operation op = AttributeModifier.Operation.valueOf(data0[1]);
                        if(op!=null && amount!=null) {
                            UUID uid = UUID.randomUUID();
                            attr.addModifier(new AttributeModifier(uid, uid.toString(), amount,op));
                            //attr.modifier

                            msg("AttrAdded -> "+attr.getAttribute().name());
                            data.add(attr.toString()+":"+uid);

                            int time = 5 ;
                            if(data0.length>=4){
                                time = safeDoubleToInt(calculateSafe(data0[3]));
                            }
                            Bukkit.getScheduler().runTaskLater(CustomEnchantments.getInstance(),()->{
                                if(attr.getModifier(uid)!=null) {
                                    attr.removeModifier(uid);
                                }
                            },time*20L);
                        }
                    }catch (Exception ex){

                    }
                }
            }
            attrModifier.put(player.getUniqueId(),data);
        }
    }

















    public static boolean isAbilityAvailable(Player player,UUID uuid,Enchant enchant,int lvl) {
        HashMap<String,String> placeH = hashMap(
                "lvl",lvl+"","level",lvl+""
        );

        String cooldown = enchant.getCooldown();
        cooldown = fixPlaceholders(cooldown,placeH);
        if (cooldown.startsWith("-")) return true;
        int c;
        try{
            c = safeDoubleToInt(calculateSafe(cooldown));
        } catch (Exception e) {
            c = (int) TimeConvert.getTime(cooldown);
        }
        return TimeConvert.isAvailable(uuid,c);
    }
    public static boolean isAbilityAvailable(Player player,UUID uuid,Enchant enchant) {
        String cooldown = enchant.getCooldown();
        if (cooldown.startsWith("-")) return true;
        int c = (int) TimeConvert.getTime(cooldown);
        return TimeConvert.isAvailable(uuid,c);
    }
}
