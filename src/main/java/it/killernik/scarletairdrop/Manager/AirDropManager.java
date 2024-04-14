package it.killernik.scarletairdrop.Manager;

import it.killernik.scarletairdrop.ScarletAirDrop;
import it.killernik.scarletairdrop.Utils.BukkitSerialization;
import it.killernik.scarletairdrop.Utils.HologramsUtils;
import it.killernik.scarletairdrop.Utils.LocationUtils;
import it.killernik.scarletairdrop.Utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AirDropManager {
    public static List<Chest> chestList = new ArrayList<>();
    public List<Location> locList = new ArrayList<>();
    public HashMap<Chest, HologramsUtils> holoMap = new HashMap<>();

    public void startEvent() {

        int maxADAmount = getMaxADAmount();
        int minADAmount = getMinADAmount();
        int airdropSpawned = 0;

        List<String> locations = ScarletAirDrop.INSTANCE.getConfig().getStringList("Locations");
        for (String locString : locations) {
            locList.add(LocationUtils.stringToLocation(locString));
        }

        if (locList.isEmpty()) {
            Bukkit.getLogger().warning("Evento fermato! Nessuna location impostata!");
            return;
        }

        if (maxADAmount > locList.size()) {
            maxADAmount = locList.size();
        }

        if (minADAmount > locList.size()) {
            minADAmount = locList.size();
        }

        int range = maxADAmount - minADAmount + 1;
        int AirDropAmount = (int) (Math.random() * range) + minADAmount;

        for (Location loc : locList) {
            spawnAirDrop(loc);
            airdropSpawned++;
            if (airdropSpawned == AirDropAmount) {
                break;
            }
        }

        Bukkit.broadcastMessage(MessageUtil.message(ScarletAirDrop.INSTANCE.getConfig().getString("Message.started").replaceAll("%airdrop%", String.valueOf(airdropSpawned))));

        int airdropExpire = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.expire");

        new BukkitRunnable() {
            @Override
            public void run() {

                for (Chest chest : chestList) {
                    removeAirdrop(chest);
                }

            }
        }.runTaskLater(ScarletAirDrop.INSTANCE, 20L * airdropExpire);

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
        int range = maxLootAmount - minLootAmount + 1;
        int lootAmount = (int) (Math.random() * range) + minLootAmount;
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

    private int getMaxADAmount() {

        int playersOnline = Bukkit.getOnlinePlayers().size();
        int maxADAmount;

        if (playersOnline <= 7) {
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

    private int getMinADAmount() {

        int playersOnline = Bukkit.getOnlinePlayers().size();
        int minADAmount;

        if (playersOnline >= 7 && playersOnline < 15) {
            minADAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.dynamic-airdrop-amount.7.max-airdrop");
            return minADAmount;
        }

        if (playersOnline <= 15) {
            minADAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.dynamic-airdrop-amount.15.max-airdrop");
            return minADAmount;
        }

        if (playersOnline <= 35) {
            minADAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.dynamic-airdrop-amount.35.max-airdrop");
            return minADAmount;
        }

        if (playersOnline <= 50) {
            minADAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.dynamic-airdrop-amount.50.max-airdrop");
            return minADAmount;
        }

        minADAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.dynamic-airdrop-amount.max-on>50");
        return minADAmount;

    }

}
