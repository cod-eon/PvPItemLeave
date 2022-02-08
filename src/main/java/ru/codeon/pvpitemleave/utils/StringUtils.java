package ru.codeon.pvpitemleave.utils;

import org.bukkit.ChatColor;

public class StringUtils {

    public static String convertString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
