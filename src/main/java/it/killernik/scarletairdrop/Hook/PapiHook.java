package it.killernik.scarletairdrop.Hook;

import it.killernik.scarletairdrop.ScarletAirDrop;
import it.killernik.scarletairdrop.Utils.MessageUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.util.Objects;

public class PapiHook extends PlaceholderExpansion {
    public String getAuthor() {
        return "killernik";
    }

    public String getIdentifier() {
        return "scarletairdrop";
    }

    public String getVersion() {
        return "1.0";
    }

    public String onRequest(OfflinePlayer player, String params) {
        if (Objects.equals(params, "countdown")) {
            return MessageUtil.formatTime(ScarletAirDrop.INSTANCE.airDropSpawnTask.getTime());
        }
        return null;
    }
}
