package org.loioh.customenchantments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.loioh.customenchantments.Utils.TimeConvert.sumNumbers;

public class Configs{
    private JavaPlugin plugin;
    private String name;
    private FileConfiguration config;
    private Boolean BaseLoad = true;


    public Configs(JavaPlugin plugin,String name) {
        this.plugin = plugin;
        this.name = name;
        load();
    }
    public Configs(JavaPlugin plugin,String name,Boolean BaseLoad) {
        this.plugin = plugin;
        this.name = name;
        this.BaseLoad = BaseLoad;
        load();
    }
    public void load(){
        java.io.File configF = new java.io.File(plugin.getDataFolder(),name+".yml");
        config = YamlConfiguration.loadConfiguration(configF);
    }


    public String get_name(){
        return name;
    }

    public Object get(String path){
        return get(path,BaseLoad);
    }
    public Object get(String path,Boolean load){ return get(path,null,load); }
    public Object get(String path,Object def){
        return get(path, def,BaseLoad);
    }
    public Object get(String path,Object def,Boolean load){
        if(load) {
            load();
        }
        Object obj = config.get(path);
        if(obj==null) {
            obj = def;
        }
        return obj;
    }






    public List<?> getList(String path){
        return  getList(path,BaseLoad);
    }
    public List<?> getList(String path,Boolean load){
        if(load) {
            load();
        }
        return config.getList(path);
    }

    public static final String site = "https://www.fiverr.com/s/3j4g7m";
    public Set<String> getCS(String path) {
        return getCS(path,BaseLoad);
    }
    public Set<String> getCS(String path,Boolean load) {
        if(load) {
            load();
        }
        ConfigurationSection c;
        if (path != null) {
            c = config.getConfigurationSection(path);
        } else {
            c = config.getConfigurationSection("");
        }

        if (c != null) {
            Set<String> cs = c.getKeys(false);
            if (cs != null && !cs.isEmpty()) {
                return cs;
            }
        }
        return null;
    }


    public Boolean contains(String path){
        return contains(path,BaseLoad);
    }
    public Boolean contains(String path,Boolean load){
        if(load) {
            load();
        }
        return config.contains(path);
    }
    public void set(String path,Object data){
        config.set(path,data);
    }
    public void saveConfig(){
        save();
    }
    public void save(){
        java.io.File configF = new java.io.File(plugin.getDataFolder(),name+".yml");
        try {
            config.save(configF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    public Object add(String path,Object amount){ return add(path, amount,BaseLoad); }
    public Object add(String path,Object amount,Boolean load){ return add(path, null,amount,load); }
    public Object add(String path,Object def,Object amount){ return add(path, def,amount,BaseLoad); }
    public Object add(String path,Object def,Object amount,Boolean load){
        def = (def == null) ? (amount instanceof Double ? 0.0 : amount instanceof Integer ? 0 : null) : def;

        if(load) {
            load();
        }

        Object current = config.get(path);
        if (current == null) {
            current = def;
        }

        if (current instanceof Integer && amount instanceof Integer) {
            int result = (Integer) current + (Integer) amount;
            config.set(path, result);
            return result;
        }else if (current instanceof Double && amount instanceof Double) {
            double result = (Double) current + (Double) amount;
            config.set(path, result);
            return result;
        }else if (current instanceof String && amount instanceof String) {
            String result = sumNumbers((String) current, (String) amount);
            config.set(path, result);
            return result;
        }
        return current;
    }




    public void setHashMapI(String path,HashMap<Integer,Object> ItemS){
        if(ItemS!=null && !ItemS.isEmpty()) {
            for (int key : ItemS.keySet()) {
                set(path + "." + key, ItemS.get(key));
            }
        }
    }
    public HashMap<Integer,Object> getHashMapI(String path){
        load();
        if(config.contains(path)) {
            HashMap<Integer, Object> hm = new HashMap<>();
            Set<String> cs = this.getCS(path);
            if (cs != null && !cs.isEmpty()) {
                for (String key : cs) {
                    try {
                        int i = Integer.parseInt(key);
                        hm.put(i, get(path + "." + key));
                    }catch(NumberFormatException e){

                    }
                }
                return hm;
            }
        }
        return  null;
    }

    public void setHashMap(String path, HashMap<String,Object> ItemS){
        if(ItemS!=null && !ItemS.isEmpty()) {
            for (String key : ItemS.keySet()) {
                set(path + "." + key, ItemS.get(key));
            }
        }
    }
    public HashMap<String,Object> getHashMap(String path){
        return getHashMap(path,BaseLoad);
    }
    public HashMap<String,Object> getHashMap(String path,Boolean load){
        if(load) {
            load();
        }
        if(config.contains(path)) {
            HashMap<String, Object> hm = new HashMap<>();
            Set<String> cs = this.getCS(path);
            if (cs != null && !cs.isEmpty()) {
                for (String key : cs) {
                    hm.put(key, get(path + "." + key));
                }
                return hm;
            }
        }
        return  null;
    }


    public void setHashMapList(String path, HashMap<String,List<Object>> ItemS){
        if(ItemS!=null && !ItemS.isEmpty()) {
            for (String key : ItemS.keySet()) {
                List<Object> l = ItemS.get(key);
                set(path + "." + key, l);
            }
        }
    }
    public HashMap<String,List<Object>> getHashMapList(String path,Boolean load){
        if(load) {
            load();
        }
        if(config.contains(path)) {
            Set<String> cs = this.getCS(path,false);
            if (cs != null && !cs.isEmpty()) {
                HashMap<String,List<Object>> hm = new HashMap<>();
                for (String key : cs) {
                    hm.put(key, (List<Object>) getList(path + "." + key,false));
                }
                return hm;
            }
        }
        return  null;
    }



}