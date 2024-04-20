package it.killernik.scarletairdrop.Manager;

import it.killernik.scarletairdrop.ScarletAirDrop;
import it.killernik.scarletairdrop.Utils.BukkitSerialization;
import it.killernik.scarletairdrop.Utils.HologramsUtils;
import it.killernik.scarletairdrop.WorkLoad.Workload;
import it.killernik.scarletairdrop.WorkLoad.impl.SpawnAirdropWorkload;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AirDropManager {
    public static List<Chest> chestList = new ArrayList<>();
    public List<Location> locList = new ArrayList<>();
    public HashMap<Chest, HologramsUtils> holoMap = new HashMap<>();
    public boolean eventRunning = false;

    public void startEvent() {
        Workload spawnAirdropWorkload = new SpawnAirdropWorkload();
        spawnAirdropWorkload.compute();
    }

    public void spawnAirDrop(Location loc) {
        Bukkit.getScheduler().runTask(ScarletAirDrop.INSTANCE, () -> {
            loc.getBlock().setType(Material.CHEST);
            Chest chest = (Chest) loc.getBlock().getState();
            chestList.add(chest);
            setAirdropLoot(chest);
            List<String> holoContent = ScarletAirDrop.INSTANCE.getConfig().getStringList("Settings.Holo");
            HologramsUtils hologram = new HologramsUtils(loc, holoContent);
            hologram.spawn();
            holoMap.put(chest, hologram);
        });
    }

    public void removeAirdrop(Chest chest) {
        Bukkit.getScheduler().runTask(ScarletAirDrop.INSTANCE, () -> {
            chestList.remove(chest);
            chest.getInventory().clear();
            chest.getBlock().setType(Material.AIR);
            HologramsUtils holo = holoMap.get(chest);
            holo.destroy();
            locList.remove(chest.getLocation());
        });
    }

    public void setAirdropLoot(Chest chest) {

        int maxLootAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.Loots.max-item");
        int minLootAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.Loots.min-item");
        Random rand = new Random();
        int lootAmount = rand.nextInt(maxLootAmount - minLootAmount + 1) + minLootAmount;
        int lootSpawned = 0;

        List<String> items = ScarletAirDrop.INSTANCE.getConfig().getStringList("Loots");
        for (String item : items) {

            int slotRange = 27;
            int slot = (int) (Math.random() * slotRange);

            chest.getInventory().setItem(slot, BukkitSerialization.itemStackFromBase64(item));
            lootSpawned++;
            if (lootSpawned == lootAmount) break;
        }

    }

    public int getMaxADAmount() {

        int playersOnline = Bukkit.getOnlinePlayers().size();
        int maxADAmount;

        if (playersOnline >= 7 && playersOnline < 15) {
            maxADAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.dynamic-airdrop-amount.7.max-airdrop");
            return maxADAmount;
        }

        if (playersOnline <= 15) {
            maxADAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.dynamic-airdrop-amount.15.max-airdrop");
            return maxADAmount;
        }

        if (playersOnline <= 35) {
            maxADAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.dynamic-airdrop-amount.35.max-airdrop");
            return maxADAmount;
        }

        if (playersOnline <= 50) {
            maxADAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.dynamic-airdrop-amount.50.max-airdrop");
            return maxADAmount;
        }

        maxADAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.dynamic-airdrop-amount.max-on>50");
        return maxADAmount;

    }

    public int getMinADAmount() {

        int playersOnline = Bukkit.getOnlinePlayers().size();
        int minADAmount;

        if (playersOnline >= 7 && playersOnline < 15) {
            minADAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.dynamic-airdrop-amount.7.min-airdrop");
            return minADAmount;
        }

        if (playersOnline <= 15) {
            minADAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.dynamic-airdrop-amount.15.min-airdrop");
            return minADAmount;
        }

        if (playersOnline <= 35) {
            minADAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.dynamic-airdrop-amount.35.min-airdrop");
            return minADAmount;
        }

        if (playersOnline <= 50) {
            minADAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.dynamic-airdrop-amount.50.min-airdrop");
            return minADAmount;
        }

        minADAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.dynamic-airdrop-amount.min-on>50");
        return minADAmount;

    }

}
