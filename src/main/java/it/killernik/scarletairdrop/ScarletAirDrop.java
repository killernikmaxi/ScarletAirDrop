package it.killernik.scarletairdrop;

import it.killernik.scarletairdrop.Commands.MainCommand;
import it.killernik.scarletairdrop.Manager.AirDropManager;
import it.killernik.scarletairdrop.Tasks.AirDropSpawnTask;
import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.plugin.java.JavaPlugin;

import static it.killernik.scarletairdrop.Manager.AirDropManager.chestList;

public final class ScarletAirDrop extends JavaPlugin {

    public static ScarletAirDrop INSTANCE;
    public AirDropSpawnTask airDropSpawnTask;
    public AirDropManager airDropManager;

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        saveDefaultConfig();
        INSTANCE = this;
        Bukkit.getLogger().info("[ScarletAirDrop] Avvio in corso");
        registerManagers();
        registerCommands();
        registerListeners();
        airDropSpawnTask.StartTask();
        Bukkit.getLogger().info("[ScarletAirDrop] abilitato con successo in " + (System.currentTimeMillis() - startTime) + "ms!");

    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[ScarletAirDrop] Disabilitato con successo!");
    }

    private void registerManagers() {
        airDropSpawnTask = new AirDropSpawnTask();
        airDropManager = new AirDropManager();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new it.killernik.scarletairdrop.Listener.InventoryCloseEvent(), this);
    }

    private void registerCommands() {
        getCommand("airdrop").setExecutor(new MainCommand());
    }

}