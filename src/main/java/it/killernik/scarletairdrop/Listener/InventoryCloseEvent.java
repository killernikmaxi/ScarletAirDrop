package it.killernik.scarletairdrop.Listener;

import it.killernik.scarletairdrop.Manager.AirDropManager;
import it.killernik.scarletairdrop.ScarletAirDrop;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Objects;

public class InventoryCloseEvent implements Listener {


    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClose(org.bukkit.event.inventory.InventoryCloseEvent event) {
        Inventory inv = event.getInventory();

        if (!(inv.getHolder() instanceof Chest)) return;
        Chest chest = (Chest) inv.getHolder();
        if (!AirDropManager.chestList.contains(chest)) return;
        if (Arrays.stream(chest.getInventory().getContents()).anyMatch(Objects::nonNull)) return;
        ScarletAirDrop.INSTANCE.airDropManager.removeAirdrop(chest);
    }
}