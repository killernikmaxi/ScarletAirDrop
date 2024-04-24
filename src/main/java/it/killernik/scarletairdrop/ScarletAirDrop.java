package it.killernik.scarletairdrop;

import it.killernik.scarletairdrop.Commands.MainCommand;
import it.killernik.scarletairdrop.Manager.AirDropManager;
import it.killernik.scarletairdrop.Tasks.AirDropSpawnTask;
import it.killernik.scarletairdrop.WorkLoad.Workload;
import it.killernik.scarletairdrop.WorkLoad.WorkloadThread;
import it.killernik.scarletairdrop.WorkLoad.impl.DespawnAirdropWorkload;
import it.killernik.scarletairdrop.WorkLoad.impl.SpawnAirdropWorkload;
import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

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
        WorkloadThread workloadThread = new WorkloadThread();
        Bukkit.getScheduler().runTaskTimer(this, workloadThread, 0, 1);
        Bukkit.getLogger().info("[ScarletAirDrop] Abilitato con successo in " + (System.currentTimeMillis() - startTime) + "ms!");
    }

    @Override
    public void onDisable() {
        if (!airDropManager.eventRunning) return;
        Bukkit.getLogger().info("[ScarletAirDrop] Disabilitato con successo!");
    }

    private void registerManagers() {
        airDropSpawnTask = new AirDropSpawnTask();
        airDropManager = new AirDropManager();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new it.killernik.scarletairdrop.Listener.InventoryCloseEvent(), this);
        Bukkit.getPluginManager().registerEvents(new it.killernik.scarletairdrop.Listener.PlayerArmorstandManipulateEvent(), this);
        Bukkit.getPluginManager().registerEvents(new it.killernik.scarletairdrop.Listener.BlockBreakEvent(), this);
    }

    private void registerCommands() {
        getCommand("airdrop").setExecutor(new MainCommand());
    }

}