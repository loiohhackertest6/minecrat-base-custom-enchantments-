package org.loioh.customenchantments.Listners;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import io.papermc.paper.event.player.PlayerShieldDisableEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.*;
import org.loioh.customenchantments.Events.EnchatmentConditionEvent;

import static org.loioh.customenchantments.CustomEnchantments.notDamageSeconds;


public class EnchantmentsListener implements org.bukkit.event.Listener {
    public static final Class availableType = Player.class;


    @EventHandler(priority = EventPriority.MONITOR)
    public void onKillLE(EntityDeathEvent e) {
        if (e.isCancelled()) return;
        if(e.getEntity().getKiller() == null) return;

        //if(!(e.getEntity() instanceof LivingEntity)) return;
        Player player = e.getEntity().getKiller();
        boolean killedPlayer = (e.getEntity() instanceof Player);
        onKill(player,killedPlayer,e.getEntity());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onKillP(PlayerDeathEvent e) {
        if (e.isCancelled()) return;

        new EnchatmentConditionEvent(e.getEntity(),"onDeath");

        if(e.getEntity().getKiller() == null) return;

        Player player = e.getEntity().getKiller();
        boolean killedPlayer = (e.getEntity() instanceof Player);
        
        onKill(player,killedPlayer,e.getEntity());
    }



    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamageE(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if(!(e.getEntity() instanceof LivingEntity)) return;
        boolean damagedPlayer = (e.getEntity() instanceof Player);
        if(damagedPlayer && !(e.getDamager() instanceof Player)){
            new EnchatmentConditionEvent((Player) e.getEntity(), "onGetDamageLivingEntity");
        }

        if(!(e.getDamager() instanceof Player)) return;
        Player player = (Player)e.getDamager();
        onDamage(player,damagedPlayer,(LivingEntity) e.getEntity());
    }


    public void onKill(Player player, boolean killPlayer, LivingEntity killed){
        if(!player.isOnline()) return;

        new EnchatmentConditionEvent(player,killPlayer? "onKillPlayer":"onKillLivingEntity");

    }
    public void onDamage(Player player, boolean damagePlayer, LivingEntity damaged) {
        if(!player.isOnline()) return;
        if(damagePlayer){
           Player p = (Player) damaged;
            new EnchatmentConditionEvent(p, "onGetDamagePlayer");
        }
        new EnchatmentConditionEvent(player,damagePlayer? "onDamagePlayer":"onDamageLivingEntity");

    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamageE2(EntityDamageEvent e) {
        if (e.isCancelled()) return;

        if(!(e.getEntity() instanceof Player)) return;
        Player player = (Player)e.getEntity();
        notDamageSeconds.put(player.getUniqueId(),0L);
        new EnchatmentConditionEvent(player,"onGetDamage");
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onProjectileA(PlayerLaunchProjectileEvent e) {
        if (e.isCancelled()) return;

        if(e.getPlayer() == null) return;
        EntityType type = e.getProjectile().getType();
        if(type.equals(EntityType.ARROW) || type.equals(EntityType.SPECTRAL_ARROW)) {
            new EnchatmentConditionEvent(e.getPlayer(), "onArrowProjectile");
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onProjectileA(EntityShootBowEvent e) {
        if (e.isCancelled()) return;

        if(!(e.getEntity() instanceof Player)) return;
        EntityType type = e.getProjectile().getType();
        if(type.equals(EntityType.ARROW) || type.equals(EntityType.SPECTRAL_ARROW)) {
            new EnchatmentConditionEvent((Player)e.getEntity(), "onArrowProjectile");
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onShieldB(PlayerShieldDisableEvent e) {
        if (e.isCancelled()) return;
        if(e.getPlayer() == null) return;
        if(e.getCooldown() <= 1) return;
        new EnchatmentConditionEvent(e.getPlayer(), "onShieldCooldownStart");

    }
}


