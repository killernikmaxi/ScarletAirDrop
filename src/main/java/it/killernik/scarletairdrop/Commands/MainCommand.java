package it.killernik.scarletairdrop.Commands;

import it.killernik.scarletairdrop.ScarletAirDrop;
import it.killernik.scarletairdrop.Utils.ItemStackUtils;
import it.killernik.scarletairdrop.Utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
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
            commandSender.sendMessage(MessageUtil.message("&c* &7/airdrop &bkillarmorstand"));
            commandSender.sendMessage(MessageUtil.message("&7&m--- --*---------------------------*-- ---"));
            return true;
        }

        if (args.length == 1) {

            switch (args[0]) {
                case "additem":
                    if (commandSender instanceof Player) {
                        Player p = (Player) commandSender;
                        ItemStack item = p.getInventory().getItemInHand();
                        if (item.getType() == Material.AIR || item.getType() == null) return true;
                        List<String> items = ScarletAirDrop.INSTANCE.getConfig().getStringList("Loots");
                        items.add(ItemStackUtils.serialize(item));
                        ScarletAirDrop.INSTANCE.getConfig().set("Loots", items);
                        ScarletAirDrop.INSTANCE.saveConfig();
                        p.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &aItem aggiunto!"));
                    } else {
                        commandSender.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &cSolo i giocatori possono eseguire questo comando!"));
                    }
                    break;
                case "getitems":
                    if (commandSender instanceof Player) {
                        Player p = (Player) commandSender;

                        List<String> items = ScarletAirDrop.INSTANCE.getConfig().getStringList("Loots");

                        for (String item : items) {
                            p.getInventory().addItem(ItemStackUtils.deserialize(item));
                        }

                        p.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &aItems ottenuti!"));

                        return true;
                    } else {
                        commandSender.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &cSolo i giocatori possono eseguire questo comando!"));
                    }
                    break;
                case "addlocation":
                    if (commandSender instanceof Player) {

                        Player p = (Player) commandSender;
                        Location loc = p.getLocation();
                        loc = loc.getBlock().getLocation().add(0.5, 0, 0.5);

                        List<String> locations = ScarletAirDrop.INSTANCE.getConfig().getStringList("Locations");
                        locations.add(locationToString(loc));
                        ScarletAirDrop.INSTANCE.getConfig().set("Locations", locations);
                        ScarletAirDrop.INSTANCE.saveConfig();

                        p.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &aLocation aggiunta!"));

                        return true;
                    } else {
                        commandSender.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &cSolo i giocatori possono eseguire questo comando!"));
                    }
                    break;
                case "reload":
                    ScarletAirDrop.INSTANCE.reloadConfig();
                    commandSender.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &aConfig ricaricato!"));
                    break;
                case "killarmorstand":
                    if (commandSender instanceof Player) {
                        for (Entity entity : ((Player) commandSender).getNearbyEntities(5, 5, 5)) {
                            if (entity instanceof ArmorStand) {
                                ArmorStand armorStand = (ArmorStand) entity;
                                armorStand.remove();
                            }
                        }
                    }
                    break;
                default:
                    commandSender.sendMessage(MessageUtil.message("&4&lAIRDROP &8// &cSintassi errata"));
                    break;
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
        return true;
    }

}
