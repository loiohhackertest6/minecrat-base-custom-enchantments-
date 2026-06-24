package org.loioh.customenchantments.Listners;

import com.google.common.collect.Multimap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;
import org.loioh.customenchantments.CustomEnchantments;
import org.loioh.customenchantments.Enchantments.Enchant;
import org.loioh.customenchantments.Events.EnchatmentConditionEvent;
import org.loioh.customenchantments.Utils.Effects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.loioh.customenchantments.CustomEnchantments.msg;
import static org.loioh.customenchantments.Utils.EnchantLoresManager.getPlayerActiveEnchantments;
import static org.loioh.customenchantments.Utils.Messages.fixPlaceholders;
import static org.loioh.customenchantments.Utils.TimeConvert.*;
import static org.loioh.customenchantments._Init.hashMap;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void onProject(ProjectileHitEvent e) {
        if(e.isCancelled()) return;
        Projectile damager = e.getEntity();

        if(damager.hasMetadata("Explosion")) {
            double strength = calculateSafe(damager.getMetadata("Explosion").get(0).asString());
            Explosion(damager.getLocation(),strength);
            damager.remove();
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onHitMob(EntityDamageByEntityEvent e){
        if(e.isCancelled()) return;
        if(!(e.getDamager() instanceof Arrow)) return;
        if(!(e.getEntity() instanceof LivingEntity)) return;
        double finalDamage = e.getFinalDamage();
        msg("OriginalDamage: "+finalDamage+" ; "+e.getDamage());


        Entity damager = e.getDamager();
        UUID owner_uuid = ((Arrow)damager).getOwnerUniqueId();

        if(damager.hasMetadata("Damage") ) {
            double damage = calculateSafe(damager.getMetadata("Damage").get(0).asString());
            e.setDamage(damage);
        }
        if(damager.hasMetadata("Explosion")) {
            double strength = calculateSafe(damager.getMetadata("Explosion").get(0).asString());
            Explosion(damager.getLocation(),strength);
            damager.remove();
            e.setCancelled(true);
        }
        if(damager.hasMetadata("ArmorIgnore")) {
            double percentRemove = calculateSafe(damager.getMetadata("ArmorIgnore").get(0).asString());
            if(e.getEntity() instanceof Player) {
                Player player = (Player)e.getEntity();
                //Effects.breachPlayer(player, percentRemove,10);
                double currentArmor = player.getAttribute(Attribute.ARMOR).getValue();
                double currentToughness = player.getAttribute(Attribute.ARMOR_TOUGHNESS).getValue();

                double baseDamage = e.getDamage(EntityDamageEvent.DamageModifier.BASE);
                double armorAbsorbed = baseDamage - finalDamage;
                double reducedArmor = currentArmor * (1.0 - percentRemove / 100.0);
                double reducedToughness = currentToughness * (1.0 - percentRemove / 100.0);
                double damageReduction = Math.max(0, reducedArmor - (baseDamage / (reducedToughness / 4.0 + 2.0)));
                damageReduction = Math.min(20, damageReduction);

                double reductionPercent = damageReduction * 0.04;
                double newArmorAbsorbed = baseDamage * reductionPercent;
                double additionalDamage = armorAbsorbed - newArmorAbsorbed;
                double new_damage = (finalDamage + additionalDamage);
                msg("Damage: "+finalDamage+"  ->  "+ new_damage);
                player.damage(additionalDamage);
            }
        }
       if(damager.hasMetadata("Magnetic")) {
           String data = damager.getMetadata("Magnetic").get(0).asString();
           double strength = 0.5;
           double distance;
           if(data.contains(";")) {
               distance = calculateSafe(data.split(";")[0]);
               strength = calculateSafe(data.split(";")[1]);
           }else{
               distance = calculateSafe(data);
           }
           Location loc = e.getEntity().getLocation().clone();

           msg("distanse;strenght ->" + distance+"; "+strength);

           Vector center = loc.toVector().clone();
           for(Entity en:loc.getNearbyEntities(distance,distance,distance)){
               if (!(en instanceof LivingEntity)) continue;
               if (en.getUniqueId().equals(e.getEntity().getUniqueId())) continue;
               if (en.getUniqueId().equals(owner_uuid)) continue;

               Vector loc0 = en.getLocation().toVector().clone();
               double d0 = center.distance(loc0);
               Vector velocity = center.clone()
                       .subtract(loc0)
                       .normalize();
               velocity = velocity.setY(0.5).normalize();

               en.setVelocity(velocity.multiply(strength * (d0/distance)));
           }
       }
    }

    public static void Explosion(Location loc, double strength){
        loc.getWorld().createExplosion(loc,(float)strength);
    }





    @EventHandler(priority = EventPriority.MONITOR)
    public void onEcnhantCondition(EnchatmentConditionEvent e) {
        if (e.isCancelled()) return;


        //msg("Event: "+e.eventText);
        Player player = e.player;
        String event = e.eventText;
        if(player!=null){
            processPlayerEnchantmentsEvent(player,event);
        }else{
            for(Player p : Bukkit.getOnlinePlayers()){
                processPlayerEnchantmentsEvent(p,event);
            }
        }

    }
    public static void processPlayerEnchantmentsEvent(Player player,String event){
        Multimap<Enchant, Map.Entry<Integer, UUID>> enchants = getPlayerActiveEnchantments(player);
        //msg("EventEnchsntments: "+enchants.size());


        for(Map.Entry<Enchant, Map.Entry<Integer, UUID>> entry:enchants.entries()) {
            Enchant ench = entry.getKey();

            HashMap<String,String> placeH = hashMap(
                    "Health",(player.getHealth()/player.getMaxHealth())+"",
                    "DamageNotTaken",CustomEnchantments.notDamageSeconds.getOrDefault(player.getUniqueId(),0L)+"",
                    "FoodLevel",(player.getFoodLevel())+"",
                    "Saturation",(player.getSaturation())+""
            );
            String condition = fixPlaceholders(ench.getActivateOn(),placeH);
            String[] conditions = null;
            if(condition.contains(";")) {
                conditions = condition.split(";");
            }else{
                conditions = new String[]{condition};
            }
            if(conditions[0].equals(event)){
                boolean apply = true;
                if(conditions.length > 1) {
                    for(int i=1;i<conditions.length;i++) {
                        String c = conditions[i];
                        msg("Condition C: "+c);
                        Boolean matched = checkExpression(c);
                        if(matched==null || !matched){
                            apply = false;
                            break;
                        }
                    }
                }
                if(!apply) continue;



                Integer lvl = entry.getValue().getKey();
                UUID uuid = entry.getValue().getValue();
                Effects.runEnchantAbilities(player,uuid,ench,lvl);
            }
        }
    }


}
