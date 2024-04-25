package it.killernik.scarletairdrop.Utils;

import org.bukkit.ChatColor;

public class StringUtil {

    public static String message(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String formatTime(int timeInSeconds) {
        if (timeInSeconds < 0) return "Caricamento...";

        int hours = timeInSeconds / 3600;
        int minutes = (timeInSeconds % 3600) / 60;
        int seconds = timeInSeconds % 60;

        StringBuilder formattedTime = new StringBuilder();

        if (hours > 0) formattedTime.append(hours).append(hours == 1 ? " ora, " : " ore, ");

        if (minutes > 0) formattedTime.append(minutes).append(minutes == 1 ? " minuto e " : " minuti e ");

        if (seconds > 0) formattedTime.append(seconds).append(seconds == 1 ? " secondo" : " secondi");

        return formattedTime.toString();
    }
}
