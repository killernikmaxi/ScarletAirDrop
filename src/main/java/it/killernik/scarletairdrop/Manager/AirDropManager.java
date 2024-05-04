package it.killernik.scarletairdrop.Manager;

import it.killernik.scarletairdrop.ScarletAirDrop;
import it.killernik.scarletairdrop.Utils.HologramsUtils;
import it.killernik.scarletairdrop.Utils.ItemStackUtils;
import it.killernik.scarletairdrop.Utils.WebHookUtils;
import it.killernik.scarletairdrop.WorkLoad.Workload;
import it.killernik.scarletairdrop.WorkLoad.impl.SpawnAirdropWorkload;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;

public class AirDropManager {
    public static List<Chest> chestList = new ArrayList<>();
    public List<Location> locList = new ArrayList<>();
    public Map<Chest, HologramsUtils> holoMap = new HashMap<>();
    public boolean eventRunning = false;
    FileConfiguration config = ScarletAirDrop.INSTANCE.getConfig();

    public void startEvent() {
        Workload spawnAirdropWorkload = new SpawnAirdropWorkload();
        spawnAirdropWorkload.compute();
        sendStartedWebHook();
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
            locList.remove(chest.getLocation());
            HologramsUtils holo = holoMap.get(chest);
            if (holo != null) holo.destroy();
            if (chest.getBlock().getType() == Material.CHEST) {
                chest.getInventory().clear();
                chest.getBlock().setType(Material.AIR);
            }
        });
    }

    public void setAirdropLoot(Chest chest) {

        List<String> items = ScarletAirDrop.INSTANCE.getConfig().getStringList("Loots");
        Collections.shuffle(items);
        int maxLootAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.Loots.max-item");
        int minLootAmount = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.Loots.min-item");
        int lootAmount = new Random().nextInt(maxLootAmount + 1 - minLootAmount) + minLootAmount;
        int lootSpawned = 0;

        for (String item : items) {

            int slotRange = 27;
            int slot = (int) (Math.random() * slotRange);
            ItemStack itemDeserialized = ItemStackUtils.deserialize(item);
            if (chest.getInventory().contains(itemDeserialized)) return;
            chest.getInventory().setItem(slot, itemDeserialized);
            lootSpawned++;
            if (lootSpawned >= lootAmount) break;
        }

    }

    public double getAirdropAmount() {
        int playersOn = Bukkit.getServer().getOnlinePlayers().size();
        double min = config.getDouble("Settings.AirDrop.min-percentage") * playersOn;
        double max = config.getDouble("Settings.AirDrop.max-percentage") * playersOn;
        return (min + (max - min) * new Random().nextDouble());
    }

    private void sendStartedWebHook() {
        if (!config.getBoolean("Webhook.enabled")) return;

        String webhookUrl = config.getString("Webhook.webhook-url");
        String username = config.getString("Webhook.username");
        String title = config.getString("Webhook.title");
        String desc = config.getString("Webhook.description");
        String avatarUrl = config.getString("Webhook.avatar-url");
        String thumbnailUrl = config.getString("Webhook.thumbnail-url");

        WebHookUtils webhook = new WebHookUtils(webhookUrl);

        webhook.setUsername(username);
        webhook.setAvatarUrl(avatarUrl);

        webhook.addEmbed(new WebHookUtils.EmbedObject()
                .setTitle(title)
                .setDescription(desc)
                .setThumbnail(thumbnailUrl));
        try {
            webhook.execute();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}
