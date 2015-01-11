package me.enterdimension.entersplots;

import me.enterdimension.entersplots.inc.area;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static me.enterdimension.entersplots.inc.area.*;

import me.enterdimension.entersplots.inc.sql;

import java.util.ArrayList;

/*

    Produced by Isaac Brown
    Last edited 11/01/2015 by Isaac

    Copyright 2015 Enters-Domain, All Rights Reserved

    Website: http://www.enters-domain.com
    License: http://www.enters-domain.com/license

*/

public class main extends JavaPlugin {
    public static Location corner1;
    public static Location corner2;
    public static Location[] corners = new Location[2];

    public void onEnable(){
        getLogger().info("Enter's Plots is now running.");
    }
    public void onDisable(){
        getLogger().info("Enter's Plots has stopped running.");
    }
    public boolean onCommand(CommandSender commandsender, Command cmd, String label, String[] args) {
        String command = cmd.getName();
        if (commandsender instanceof Player && command.equalsIgnoreCase("plots")) {
            Player sender = (Player) commandsender;
            if (args.length == 0) {
                sender.sendMessage(ChatColor.DARK_AQUA + ""
                        + ChatColor.BOLD + "Enter's Plot Plugin successfully built and running\n" + ChatColor.RESET
                        + ChatColor.DARK_AQUA + "Version: " + ChatColor.RESET + ChatColor.AQUA + "p1.1.0\n"
                        + ChatColor.DARK_AQUA+"Help: " + ChatColor.RESET+ChatColor.AQUA + "/plots help");
            } else {
                if (args[0].equalsIgnoreCase("help")) {
                    String[] help = {"&6/plots help:&r Returns all commands and descriptions for Enter's Plot Plugin",
                            "&6/plots select [1 or 2]:&r Selects a corner from your current position to draw an area for a plot selection.",
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
                            sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED
                                    + "Please use the correct syntax: /plots select [1 or 2]");
                        } else {
                            if (args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("2")) {
                                Location l = sender.getLocation();
                                sender.sendMessage(ChatColor.DARK_AQUA +"[Plots]" + ChatColor.AQUA
                                        + " Selected corner number " + args[1] + " @(" + l.getBlockX()+", "
                                        + l.getBlockY() + ", " + l.getBlockZ() + ")");
                                corners[(Integer.parseInt(args[1])) - 1] = l;
                            } else {
                                sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RESET + ChatColor.RED
                                        + "Please use the correct syntax: /plots select [1 or 2]");
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("claim")) {
                        ArrayList<ArrayList<String>> results = sql.getQuery("SELECT * FROM plots");
                        sql.query("INSERT INTO plots (Owner,corner1x,corner1y,corner1z,corner2x,corner2y,corner2z) " +
                                "VALUES ('" + sender.getName() + "','" +
                                corners[0].getBlockX() + "','" +
                                corners[0].getBlockY() + "','" +
                                corners[0].getBlockZ() + "','" +
                                corners[1].getBlockX() + "','" +
                                corners[1].getBlockY() + "','" +
                                corners[1].getBlockZ() + "');");
                        sender.sendMessage(ChatColor.DARK_AQUA + "[Plots]" + ChatColor.AQUA + " Successfully claimed "
                                + "(" + area.getColumnCount(corners[0],corners[1]) + ")" + " columns under plot "
                                + Integer.toString(results.size() + 1) + ". Type /plots options for all plot actions.");
                    }
                }
            }
        }
        return true;
    }
}
