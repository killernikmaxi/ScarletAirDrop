package it.killernik.scarletairdrop.Listener;

import it.killernik.scarletairdrop.Manager.AirDropManager;
import it.killernik.scarletairdrop.ScarletAirDrop;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BlockBreakEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent e) {
        Block block = e.getBlock();
        if (!(block.getState() instanceof Chest)) return;
        Chest chest = (Chest) block.getState();
        if (!AirDropManager.chestList.contains(chest)) return;
        ScarletAirDrop.INSTANCE.airDropManager.removeAirdrop(chest);
    }
}
