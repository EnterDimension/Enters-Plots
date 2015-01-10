package me.enterdimension.entersplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static me.enterdimension.entersplugin.inc.area.*;
import static org.bukkit.Bukkit.getLogger;

import me.enterdimension.entersplugin.inc.sql;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Isaac on 08/01/2015.
 *
 */
public class main extends JavaPlugin {
    public static Location corner1;
    public static Location corner2;
    public static Location[] corners = new Location[2];

    public void onEnable(){
        getLogger().info("Enter's Plugin is now running.");
    }
    public void onDisable(){
        getLogger().info("Enter's Plugin has stopped running.");
    }
    public boolean onCommand(CommandSender commandsender, Command cmd, String label, String[] args) {
        String command = cmd.getName();
        if (commandsender instanceof Player && command.equalsIgnoreCase("plots")) {
            Player sender = (Player) commandsender;
            if (args.length == 0) {
                sender.sendMessage(ChatColor.DARK_AQUA + ""
                        + ChatColor.BOLD + "Enter's Plot Plugin successfully built and running\n"
                        + ChatColor.RESET + "" + ChatColor.DARK_AQUA + "Version: " + ChatColor.RESET + "p1.1.0\n"
                        + ChatColor.DARK_AQUA + "Help: " + ChatColor.RESET + "/plots help");
            } else {
                if (args[0].equalsIgnoreCase("help")) {
                    String[] help = {"&6/plots help:&r Returns all commands and descriptions for Enter's Plot Plugin",
                            "&6/plots select [x or y]:&r Selects a corner from your current position to draw an area for a plot selection.",
                            "&6/plots claim:&r Claims your current plot selection. Requires at least 2 corners at diagonals and must be at least ??x??",
                            "&6/plots options:&r Displays all options for the plot you are currently standing on.",
                            "&6/plots sell:&r Sells the current plot you are standing on if you are the owner.",
                            "&6/plots add [player]:&r Adds a player to the current plot you are standing on.",
                            "&6/plots remove [player]:&r Removes a player from the current plot you are standing on.",
                            "&6/plots name [name]:&r Assigns a name to the current plot you are standing on.",
                            "&6/plots lookup [player or name]:&r Searches for information on a plot. Search by player owner or plot name",
                            "&6/plots addOwner [player]:&r Adds a player and grants them full ownership permissions of the plot to the current plot you are standing on.",
                            "&6/plots removeOwner [player]:&r Removes a player  and strips them of any permissions related to the current plot are standing on.",
                            "&6/plots lock [password,group or player]:&r Locks a plot to everyone but the claimer, any owners or anyone with permission(based on lock type).",
                            "&6/plots unlock:&r Removes any current lock from the current plot you are standing on.",
                            "&6/plots destroy:&r Destroys all plot protection on the current plot you are standing on or by name",
                    };
                    for (String s : help) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                    }
                } else {
                    if (args[0].equalsIgnoreCase("select")) {
                        if (args.length == 1) {
                            sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "Please use the correct syntax: /plots select [1 or 2]");
                        } else {
                            if (args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("2")) {
                                Location l = sender.getLocation();
                                sender.sendMessage(ChatColor.DARK_AQUA +"[Plots]"+ChatColor.AQUA+" Selected corner number "+args[1]+" @(" + l.getBlockX()+", "+l.getBlockY()+", "+l.getBlockZ()+")");
                                corners[(Integer.parseInt(args[1])) - 1] = l;
                            } else {
                                sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + ChatColor.ITALIC + "(ErrCode:i002)" + ChatColor.RESET + ChatColor.RED + "Please use the correct syntax: /plots select [1 or 2]");
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("claim")) {
                        ArrayList<ArrayList<String>> results = sql.getQuery("SELECT * FROM plots");
                        String plotId = Integer.toString(results.size());
                        sql.query("INSERT INTO plots (Owner,corner1x,corner1y,corner1z,corner2x,corner2y,corner2z) " +
                                "VALUES ('" + sender.getName() + "','" + plotId +
                                corners[0].getBlockX() + "','" +
                                corners[0].getBlockY() + "','" +
                                corners[0].getBlockZ() + "','" +
                                corners[1].getBlockX() + "','" +
                                corners[1].getBlockY() + "','" +
                                corners[1].getBlockZ() + "');");
                        sender.sendMessage(ChatColor.DARK_AQUA+"[Plots]"+ChatColor.AQUA+" Successfully claimed "+"(???)"+ " columns under plot "+plotId+". Type /plots options for all plot actions.");

                    }
                    else if (args[0].equalsIgnoreCase("test")){
                        ArrayList<ArrayList<String>> results = sql.getQuery("SELECT * FROM plots WHERE ID='"+args[1]+"'");

                        //ArrayList<String> row = results.get(1);
                        Location ecorner1 = new Location(sender.getWorld(),
                                Double.parseDouble(results.get(0).get(1)),
                                Double.parseDouble(results.get(0).get(2)),
                                        Double.parseDouble(results.get(0).get(3)));
                        sender.sendMessage("test");
                        Location ecorner2 = new Location(sender.getWorld(),
                                Double.parseDouble(results.get(0).get(4)),
                                Double.parseDouble(results.get(0).get(5)),
                                Double.parseDouble(results.get(0).get(6)));
                        sender.sendMessage("test2");
                        if(inArea(sender.getWorld(),ecorner1,ecorner2,sender.getLocation())){
                            sender.sendMessage("You are on le plot");
                        }
                        else {
                            sender.sendMessage("You are not on le plot");
                        }
                    }
                }
            }
        }
        return true;
    }
}
