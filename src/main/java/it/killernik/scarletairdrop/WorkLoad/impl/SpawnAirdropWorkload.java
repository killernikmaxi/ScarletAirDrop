package it.killernik.scarletairdrop.WorkLoad.impl;

import it.killernik.scarletairdrop.Manager.AirDropManager;
import it.killernik.scarletairdrop.ScarletAirDrop;
import it.killernik.scarletairdrop.Utils.LocationUtils;
import it.killernik.scarletairdrop.Utils.MessageUtil;
import it.killernik.scarletairdrop.WorkLoad.Workload;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Chest;

import java.util.List;
import java.util.Random;

public class SpawnAirdropWorkload implements Workload {
    @Override
    public void compute() {
        AirDropManager airDropManager = ScarletAirDrop.INSTANCE.airDropManager;

        int maxADAmount = airDropManager.getMaxADAmount();
        int minADAmount = airDropManager.getMinADAmount();
        int airdropSpawned = 0;

        List<String> locations = ScarletAirDrop.INSTANCE.getConfig().getStringList("Locations");
        for (String locString : locations) {
            airDropManager.locList.add(LocationUtils.stringToLocation(locString));
        }

        if (airDropManager.locList.isEmpty()) {
            Bukkit.getLogger().warning("Evento fermato! Nessuna location impostata!");
            return;
        }

        if (maxADAmount > airDropManager.locList.size()) {
            maxADAmount = airDropManager.locList.size();
        }

        if (minADAmount > airDropManager.locList.size()) {
            minADAmount = airDropManager.locList.size();
        }

        Random rand = new Random();
        int AirDropAmount = rand.nextInt(maxADAmount - minADAmount + 1) + minADAmount;

        for (Location loc : airDropManager.locList) {
            airDropManager.spawnAirDrop(loc);
            airdropSpawned++;
            if (airdropSpawned == AirDropAmount) {
                break;
            }
        }

        Bukkit.broadcastMessage(MessageUtil.message(ScarletAirDrop.INSTANCE.getConfig().getString("Message.started").replaceAll("%airdrop%", String.valueOf(airdropSpawned))));
        int airdropExpire = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.expire");

        Bukkit.getScheduler().runTaskLater(ScarletAirDrop.INSTANCE, () -> {
            for (Chest chest : AirDropManager.chestList) {
                airDropManager.removeAirdrop(chest);
            }
        }, 20L * airdropExpire);
    }
}

