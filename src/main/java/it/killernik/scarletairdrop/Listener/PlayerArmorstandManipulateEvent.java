package it.killernik.scarletairdrop.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class PlayerArmorstandManipulateEvent implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent e) {
        if (!e.getRightClicked().isVisible()) e.setCancelled(true);
    }
}
