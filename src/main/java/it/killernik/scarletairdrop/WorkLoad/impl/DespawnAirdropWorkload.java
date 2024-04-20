package it.killernik.scarletairdrop.WorkLoad.impl;

import it.killernik.scarletairdrop.Manager.AirDropManager;
import it.killernik.scarletairdrop.ScarletAirDrop;
import it.killernik.scarletairdrop.WorkLoad.Workload;
import org.bukkit.block.Chest;

public class DespawnAirdropWorkload implements Workload {
    @Override
    public void compute() {
        AirDropManager airDropManager = ScarletAirDrop.INSTANCE.airDropManager;
        for (Chest chest : AirDropManager.chestList) {
            airDropManager.removeAirdrop(chest);
            airDropManager.eventRunning = false;
        }
    }
}
