package org.loioh.customenchantments.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EnchatmentConditionEvent extends Event implements Cancellable {
    public String eventText;
    public Player player;
    public EnchatmentConditionEvent(Player player, String text){
        this.player = player;
        this.eventText = text;
        call(this);
    }

    public EnchatmentConditionEvent(Player player, String text,boolean autoCall){
        this.player = player;
        this.eventText = text;
        if(autoCall) call(this);
    }
    public static void call(Event event){
        Bukkit.getPluginManager().callEvent(event);
    }


    private boolean canceled = false;
    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean canceled) {
        this.canceled = canceled;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
