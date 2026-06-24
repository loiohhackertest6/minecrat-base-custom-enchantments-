package org.loioh.customenchantments.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.loioh.customenchantments.Configs;
import org.loioh.customenchantments.CustomEnchantments;
import org.loioh.customenchantments.Enchantments.Enchant;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.util.*;

import static java.lang.System.currentTimeMillis;
import static org.loioh.customenchantments.CustomEnchantments.msg;
import static org.loioh.customenchantments._Init.hashMap;

public class TimeConvert {
    public static long getTime(String text) {
        String tet = text;
        int x = 1;
        if (tet.endsWith("s")) {
            tet = tet.replace("s", "");
        } else if (tet.endsWith("m")) {
            tet = tet.replace("m", "");
            x = 60;
        } else if (tet.endsWith("h")) {
            tet = tet.replace("h", "");
            x = 3600;
        } else if (tet.endsWith("d")) {
            tet = tet.replace("d", "");
            x = 24*3600;
        }

        msg("tet:" + tet + " x " + x);
        try {
            long time = safeDoubleToInt(getNumber(tet)) * x;
            return time;
        } catch (NumberFormatException ex) {
            return -1;
        }
    }


    public static long getTime(long seconds, String mode) {
        if (seconds <= 0 || mode == null) return seconds;

        switch (mode.toLowerCase()) {
            case "d":
                return seconds / (24 * 3600);
            case "h":
                return seconds / 3600;
            case "m":
                return seconds / 60;
            case "s":
                return seconds;
            case "hh":
                return (seconds % (24 * 3600)) / 3600;
            case "mm":
                return (seconds % 3600) / 60;
            case "ss":
                return seconds % 60;
            default:
                return seconds;
        }
    }

    public static String formatTime(long seconds) {
        if (seconds <= 0) return "00:00";

        long days = seconds / (24 * 3600);
        seconds %= (24 * 3600);
        long hours = seconds / 3600;
        seconds %= 3600;

        long minutes = seconds / 60;
        long secs = seconds % 60;

        if (days > 0) {
            return String.format("%dd %02d:%02d:%02d", days, hours, minutes, secs);
        } else {
            return String.format("%02d:%02d:%02d", hours, minutes, secs);
        }
    }








    public static HashMap<UUID, Long> Cooldowns = new HashMap<>();
    public static int not_reloadingS(UUID uuid, int cooldown){
        if(uuid != null) {
            if (Cooldowns.containsKey(uuid)) {
                long l = currentTimeMillis() - Cooldowns.get(uuid);
                //msg("shoot time passed: "+l);
                if (l < 0 || l > cooldown) {
                    return -1;
                } else {
                    return (int)l;
                }
            } else {
                return -1;
            }
        }
        return cooldown;
    }
    public static boolean isAvailable(UUID uuid,int cooldown) {
        int rt = not_reloadingS(uuid,cooldown);
        if (rt == -1 || cooldown == 0) {

            Cooldowns.put(uuid, currentTimeMillis());
            return true;
        } else {
            int s = (int) ((cooldown - rt) / 1000.0);
            msg("Cooldown: " + s + "s");
        }
        return false;
    }













    //MATH
    public static Double getNumber(String text){
        Double a = null;
        try{
            a = (double)(Integer.parseInt(text));
        }catch (NumberFormatException ex){
            try{
                a = Double.parseDouble(text);
            }catch (NumberFormatException ex2){
                //not coverted to number - null
            }
        }
        return a;
    }
    public static int safeDoubleToInt(Double value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (value.isInfinite() || value.isNaN()) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Value is outside the range of int type");
        }

        return value.intValue();
    }

    public static String sumNumbers(String a,String b){
        String result= null;
        double r = -1;
        String symbol = "";

        if(a.endsWith("%") && b.endsWith("%")){
            symbol = "%";
            r = getNumber(a.replace("%",""))+getNumber(b.replace("%",""));
        }else{
            if(getNumber(a)!= -1 || getNumber(b)!= -1) {
                r = getNumber(a) + getNumber(b);
            }else{
                //NumberFormatException Error
            }
        }

        if(r!=-1) {
            result = (r == (int) r) ? ((int) r) + symbol : r + symbol;
        }
        return result;
    }


    public static Double calculateSafe(String text) {
        Double result = calculate(text);
        if (result != null) {
            return result;
        }
        return getNumber(text);
    }
    public static Double calculate(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        text = text.trim().replaceAll("\\s+", "");

        try {
            while (text.contains("^")) {
                text = processPower(text);
            }

            while (text.contains("*") || text.contains("/")) {
                text = processMultDiv(text);
            }

            while (text.contains("+") || (text.lastIndexOf("-") > 0 && text.substring(1).contains("-"))) {
                text = processAddSub(text);
            }

            return Double.parseDouble(text);

        } catch (Exception e) {
            return null;
        }
    }

    private static String processPower(String text) {
        int pos = text.indexOf("^");
        if (pos == -1) return text;

        int leftStart = findNumberStart(text, pos - 1);
        int rightEnd = findNumberEnd(text, pos + 1);

        double left = Double.parseDouble(text.substring(leftStart, pos));
        double right = Double.parseDouble(text.substring(pos + 1, rightEnd));
        double result = Math.pow(left, right);

        return text.substring(0, leftStart) + result + text.substring(rightEnd);
    }

    private static String processMultDiv(String text) {
        int mulPos = text.indexOf("*");
        int divPos = text.indexOf("/");

        int pos = -1;
        char op = ' ';

        if (mulPos != -1 && divPos != -1) {
            pos = Math.min(mulPos, divPos);
            op = text.charAt(pos);
        } else if (mulPos != -1) {
            pos = mulPos;
            op = '*';
        } else if (divPos != -1) {
            pos = divPos;
            op = '/';
        }

        if (pos == -1) return text;

        int leftStart = findNumberStart(text, pos - 1);
        int rightEnd = findNumberEnd(text, pos + 1);

        double left = Double.parseDouble(text.substring(leftStart, pos));
        double right = Double.parseDouble(text.substring(pos + 1, rightEnd));
        double result = (op == '*') ? left * right : left / right;

        return text.substring(0, leftStart) + result + text.substring(rightEnd);
    }

    private static String processAddSub(String text) {
        int addPos = text.indexOf("+");
        int subPos = text.lastIndexOf("-");

        if (subPos == 0) {
            subPos = text.indexOf("-", 1);
        }

        int pos = -1;
        char op = ' ';

        if (addPos != -1 && subPos != -1) {
            pos = Math.min(addPos, subPos);
            op = text.charAt(pos);
        } else if (addPos != -1) {
            pos = addPos;
            op = '+';
        } else if (subPos != -1) {
            pos = subPos;
            op = '-';
        }

        if (pos == -1) return text;

        int leftStart = findNumberStart(text, pos - 1);
        int rightEnd = findNumberEnd(text, pos + 1);

        double left = Double.parseDouble(text.substring(leftStart, pos));
        double right = Double.parseDouble(text.substring(pos + 1, rightEnd));
        double result = (op == '+') ? left + right : left - right;

        return text.substring(0, leftStart) + result + text.substring(rightEnd);
    }

    private static int findNumberStart(String text, int pos) {
        while (pos > 0 && (Character.isDigit(text.charAt(pos - 1)) || text.charAt(pos - 1) == '.')) {
            pos--;
        }
        if (pos > 0 && text.charAt(pos - 1) == '-') {
            pos--;
        }
        return pos;
    }

    private static int findNumberEnd(String text, int pos) {
        while (pos < text.length() && (Character.isDigit(text.charAt(pos)) || text.charAt(pos) == '.')) {
            pos++;
        }
        return pos;
    }

    public static Boolean checkExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return null;
        }

        expression = expression.trim();

        try {
            String operator = null;
            int operatorPos = -1;

            if (expression.contains(">=")) {
                operator = ">=";
                operatorPos = expression.indexOf(">=");
            } else if (expression.contains("<=")) {
                operator = "<=";
                operatorPos = expression.indexOf("<=");
            } else if (expression.contains("==")) {
                operator = "==";
                operatorPos = expression.indexOf("==");
            } else if (expression.contains("!=")) {
                operator = "!=";
                operatorPos = expression.indexOf("!=");
            } else if (expression.contains(">")) {
                operator = ">";
                operatorPos = expression.indexOf(">");
            } else if (expression.contains("<")) {
                operator = "<";
                operatorPos = expression.indexOf("<");
            }

            if (operator == null) {
                return null;
            }

            String leftPart = expression.substring(0, operatorPos).trim();
            String rightPart = expression.substring(operatorPos + operator.length()).trim();

            if (leftPart.isEmpty() || rightPart.isEmpty()) {
                return null;
            }

            Double leftValue = calculate(leftPart);
            Double rightValue = calculate(rightPart);

            if (leftValue == null || rightValue == null) {
                return null;
            }

            switch (operator) {
                case ">":
                    return leftValue > rightValue;
                case "<":
                    return leftValue < rightValue;
                case ">=":
                    return leftValue >= rightValue;
                case "<=":
                    return leftValue <= rightValue;
                case "==":
                    return Math.abs(leftValue - rightValue) < 0.0000001;
                case "!=":
                    return Math.abs(leftValue - rightValue) >= 0.0000001;
                default:
                    return null;
            }

        } catch (Exception e) {
            return null;
        }
    }
}
