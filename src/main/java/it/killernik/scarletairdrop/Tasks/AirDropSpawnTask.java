package it.killernik.scarletairdrop.Tasks;

import it.killernik.scarletairdrop.ScarletAirDrop;
import it.killernik.scarletairdrop.Utils.MessageUtil;
import it.killernik.scarletairdrop.WorkLoad.Workload;
import it.killernik.scarletairdrop.WorkLoad.impl.DespawnAirdropWorkload;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class AirDropSpawnTask {

    private int secondsUntilEvent = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.cooldown");

    public int getTime() {
        return secondsUntilEvent;
    }

    public void setTime(int time) {
        this.secondsUntilEvent = time;
    }

    public void StartTask() {

        new BukkitRunnable() {
            @Override
            public void run() {
                secondsUntilEvent--;

                List<Integer> secondToBroadcast = ScarletAirDrop.INSTANCE.getConfig().getIntegerList("Settings.cooldown-message");
                if (secondToBroadcast.contains(secondsUntilEvent)) {
                    Bukkit.broadcastMessage(MessageUtil.message(ScarletAirDrop.INSTANCE.getConfig().getString("Message.starting").replaceAll("%cooldown%", MessageUtil.formatTime(secondsUntilEvent))));
                }

                if (secondsUntilEvent <= 0) {
                    if (!ScarletAirDrop.INSTANCE.getConfig().getBoolean("Settings.BETA") && Bukkit.getOnlinePlayers().size() < 7) {
                        Bukkit.broadcastMessage(MessageUtil.message(ScarletAirDrop.INSTANCE.getConfig().getString("Message.too-low-players")));
                        secondsUntilEvent = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.cooldown");
                        return;
                    }

                    if (ScarletAirDrop.INSTANCE.airDropManager.eventRunning) {
                        Workload despawnAirdropWorkload = new DespawnAirdropWorkload();
                        despawnAirdropWorkload.compute();
                    }

                    secondsUntilEvent = ScarletAirDrop.INSTANCE.getConfig().getInt("Settings.AirDrop.cooldown");
                    ScarletAirDrop.INSTANCE.airDropManager.startEvent();
                }
            }
        }.runTaskTimerAsynchronously(ScarletAirDrop.INSTANCE, 20L, 20L);
    }
}
