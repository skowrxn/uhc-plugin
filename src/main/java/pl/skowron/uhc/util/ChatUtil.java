package pl.skowron.uhc.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ChatUtil {

    public static String fixColor(String string){
        return ChatColor.translateAlternateColorCodes('&', string.replace(">>", "»"));
    }

    public static void debug(String string) { Bukkit.broadcastMessage(fixColor("&4DEBUG: &c" + string)); }

}
