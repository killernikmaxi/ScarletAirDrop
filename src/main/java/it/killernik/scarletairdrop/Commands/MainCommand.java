package it.killernik.scarletairdrop.Commands;

import it.killernik.scarletairdrop.ScarletAirDrop;
import it.killernik.scarletairdrop.Utils.BukkitSerialization;
import it.killernik.scarletairdrop.Utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static it.killernik.scarletairdrop.Utils.LocationUtils.locationToString;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (!commandSender.hasPermission("ad.admin")) {
            commandSender.sendMessage(MessageUtil.message("&cScarletAirDrop by MRkillernik"));
            commandSender.sendMessage(MessageUtil.message("&cDiscord: @ mrkillernik"));
            return true;
        }

        if (args.length == 0) {
            commandSender.sendMessage(MessageUtil.message("&7&m--- --*---------------------------*-- ---"));
            commandSender.sendMessage(MessageUtil.message("&4&lSCARLET AIRDROP - HELP"));
            commandSender.sendMessage(MessageUtil.message("&7&oAlias: /ad, /airdrop"));
            commandSender.sendMessage("");
            commandSender.sendMessage(MessageUtil.message("&4Item"));
            commandSender.sendMessage(MessageUtil.message("&c* &7/airdrop &badditem"));
            commandSender.sendMessage(MessageUtil.message("&c* &7/airdrop &bgetitems"));
            commandSender.sendMessage("");
            commandSender.sendMessage(MessageUtil.message("&4Location"));
            commandSender.sendMessage(MessageUtil.message("&c* &7/airdrop &baddlocation"));
            commandSender.sendMessage("");
            commandSender.sendMessage(MessageUtil.message("&4Altro"));
            commandSender.sendMessage(MessageUtil.message("&c* &7/airdrop &breload"));
            commandSender.sendMessage(MessageUtil.message("&c* &7/airdrop &bsetcountdown [int]"));
            commandSender.sendMessage(MessageUtil.message("&c* &7/airdrop &bspawnairdrop"));
            commandSender.sendMessage(MessageUtil.message("&7&m--- --*---------------------------*-- ---"));
            return true;
        }

        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("additem")) {
                if (commandSender instanceof Player) {
                    Player p = (Player) commandSender;
                    ItemStack item = p.getInventory().getItemInHand();
                    if (item.getType() == Material.AIR || item.getType() == null) return true;

                    List<String> items = ScarletAirDrop.INSTANCE.getConfig().getStringList("Loots");
                    items.add(BukkitSerialization.itemStackToBase64(item));
                    ScarletAirDrop.INSTANCE.getConfig().set("Loots", items);
                    ScarletAirDrop.INSTANCE.reloadConfig();

                    p.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &aItem aggiunto!"));


                    return true;
                } else {
                    commandSender.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &cSolo i giocatori possono eseguire questo comando!"));
                }
            }

            if (args[0].equalsIgnoreCase("getitems")) {
                if (commandSender instanceof Player) {
                    Player p = (Player) commandSender;

                    List<String> items = ScarletAirDrop.INSTANCE.getConfig().getStringList("Loots");

                    for (String item : items) {
                        p.getInventory().addItem(BukkitSerialization.itemStackFromBase64(item));
                    }

                    p.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &aItems ottenuti!"));

                    return true;
                } else {
                    commandSender.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &cSolo i giocatori possono eseguire questo comando!"));
                }
            }

            if (args[0].equalsIgnoreCase("addlocation")) {
                if (commandSender instanceof Player) {

                    Player p = (Player) commandSender;
                    Location loc = p.getLocation();
                    loc = loc.getBlock().getLocation().add(0.5, 0, 0.5);

                    List<String> locations = ScarletAirDrop.INSTANCE.getConfig().getStringList("Locations");
                    locations.add(locationToString(loc));
                    ScarletAirDrop.INSTANCE.getConfig().set("Locations", locations);
                    ScarletAirDrop.INSTANCE.reloadConfig();

                    p.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &aLocation aggiunta!"));

                    return true;
                } else {
                    commandSender.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &cSolo i giocatori possono eseguire questo comando!"));
                }
            }

            if (args[0].equalsIgnoreCase("spawnairdrop")) {
                if (commandSender instanceof Player) {
                    Player p = (Player) commandSender;
                    Location loc = p.getLocation();
                    loc = loc.getBlock().getLocation().add(0.5, 0, 0.5);
                    ScarletAirDrop.INSTANCE.airDropManager.spawnAirDrop(loc);
                    p.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &aAirdrop spawnato!"));
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("reload")) {
                ScarletAirDrop.INSTANCE.reloadConfig();
                commandSender.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &aConfig ricaricato!"));
                return true;

            } else {
                commandSender.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &cSintassi errata"));
                return true;
            }

        } else if (args.length == 2) {

            if (args[0].equalsIgnoreCase("setcountdown")) {
                ScarletAirDrop.INSTANCE.airDropSpawnTask.setTime(Integer.parseInt(args[1]));
                commandSender.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &aCountdown impostato con successo a " + args[1]));
                return true;
            } else {
                commandSender.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &cSintassi errata"));
                return true;
            }

        } else {
            commandSender.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &cSintassi errata"));
            return true;
        }
    }

}
