package org.loioh.customenchantments.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.loioh.customenchantments.CustomEnchantments;
import org.loioh.customenchantments.Configs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//import static org.loioh.customenchantments.Utils.APIs.Operations.*;

public class Players {
    public static OfflinePlayer getOPlayer(String playerName){
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
        if(player==null){
            //player = Bukkit.getOfflinePlayerIfCached(playerName);
            //if(player==null) {
                try {
                    UUID uuid = UUID.fromString(playerName);
                    if (uuid != null) {
                        player = Bukkit.getOfflinePlayer(uuid);
                    }
                }catch (IllegalArgumentException ex){

                }
            //}
        }
        return player;
    }

    public static Player getPlayer(String playerName){
        Player player = Bukkit.getPlayer(playerName);
        if(player==null){
            //player = Bukkit.getPlayerExact(playerName);
            //if(player==null) {
            try {
                UUID uuid = UUID.fromString(playerName);
                if (uuid != null) {
                    player = Bukkit.getPlayer(uuid);
                }
            }catch (IllegalArgumentException ex){

            }
            //}
        }
        return player;
    }












    //LOcations

    public static Location loc_pos(String com){
        String[] s = com.replace(",",".").split("'");
        Double[] c = new Double[3];
        for(int i =1;i<4;i++){
            c[i-1] = Double.parseDouble(s[i]);
        }
        Location loc = new Location(Bukkit.getWorld(s[0]),c[0],c[1],c[2]);
        return loc;
    }
    public static String loc_pos(Location loc){
        String m =  String.join("'",loc.getWorld().getName(),loc.getX()+"",loc.getY()+"",loc.getZ()+"");
        return m.replace(".",",");
    }
    public static Location loc(String com){
        String[] s = com.replace(",",".").split("'");
        Double[] c = new Double[5];
        for(int i =1;i<6;i++){
            c[i-1] = Double.parseDouble(s[i]);
        }
        Location loc = new Location(Bukkit.getWorld(s[0]),
                c[0],
                c[1],
                c[2],
                c[3].floatValue(),
                c[4].floatValue()
        );
        return loc;
    }
    public static String loc(Location loc){
        String m =  String.join("'",loc.getWorld().getName(),loc.getX()+"",loc.getY()+"",loc.getZ()+"",loc.getYaw()+"",loc.getPitch()+"");
        return m.replace(".",",");
    }

    public static Location roundLoc(Location loc){
        if(loc!=null) {
            loc = loc.clone();
            loc.setX(round(loc.getX(), 2));
            loc.setY(round(loc.getY(), 2));
            loc.setZ(round(loc.getZ(), 2));
            loc.setYaw((float) round(loc.getYaw(), 2));
            loc.setPitch((float) round(loc.getPitch(), 2));
            //loc.getDirection().normalize();
        }
        return loc;
    }
    public static double round(double value,int symbols_after){
        double rounded = new BigDecimal(value)
                .setScale(symbols_after, RoundingMode.HALF_UP)
                .doubleValue();
        return rounded;
    }
}
