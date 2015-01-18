package me.enterdimension.entersplots;

import me.enterdimension.entersplots.inc.plots;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.enterdimension.entersplots.inc.area;
import me.enterdimension.entersplots.inc.sql;

import java.util.ArrayList;
import java.util.List;

/*

    Produced by Isaac Brown
    Last edited 11/01/2015 by Isaac

    Copyright 2015 Enters-Domain, All Rights Reserved

    Website: http://www.enters-domain.com
    License: http://www.enters-domain.com/license

*/

public class main extends JavaPlugin {
    public static Location[] corners = new Location[2];

    public void onEnable(){
        getLogger().info("Enter's Plots is now running.");
        new playerListener(this);
    }
    public void onDisable(){
        getLogger().info("Enter's Plots has stopped running.");
    }
    public boolean onCommand(CommandSender commandsender, Command cmd, String label, String[] args) {
        String command = cmd.getName();
        if (commandsender instanceof Player && command.equalsIgnoreCase("plots")) {
            Player sender = (Player) commandsender;
            commandLoop:
            if (args.length == 0) {
                sender.sendMessage(ChatColor.DARK_AQUA + ""
                        + ChatColor.BOLD + "Enter's Plot Plugin successfully built and running\n" + ChatColor.RESET
                        + ChatColor.DARK_AQUA + "Version: " + ChatColor.RESET + ChatColor.AQUA + "p1.1.0\n"
                        + ChatColor.DARK_AQUA+"Help: " + ChatColor.RESET+ChatColor.AQUA + "/plots help");
            } else if (args[0].equalsIgnoreCase("help")) {
                String[] help = {"&6/plots help:&r Returns all commands and descriptions for Enter's Plot Plugin",
                        "&6/plots select [1 or 2]:&r Selects a corner from your current position to draw an area for a plot selection.",
                        "&6/plots claim:&r Claims your current plot selection. Requires at least 2 corners at diagonals and must be at least ??x??",
                        "&6/plots options:&r Displays all options for the plot you are currently standing on.",
                        "&6/plots sell:&r Sells the current plot you are standing on if you are the owner.",
                        "&6/plots add [player]:&r Adds a player to the current plot you are standing on.",
                        "&6/plots remove [player]:&r Removes a player from the current plot you are standing on.",
                        "&6/plots name [name]:&r Assigns a name to the current plot you are standing on.",
                        "&6/plots lookup [plot ID or name]:&r Searches for information on a plot. Search by plot ID or plot name",
                };
                String[] adminHelp = {
                        "&6/plots addOwner [player]:&r Adds a player and grants them full ownership permissions of the plot to the current plot you are standing on.",
                        "&6/plots removeOwner [player]:&r Removes a player  and strips them of any permissions related to the current plot are standing on.",
                        "&6/plots lock [lock type]:&r Locks a plot to everyone but the claimer, any owners or anyone with permission(based on lock type).",
                        "&6/plots unlock:&r Removes any current lock from the current plot you are standing on.",
                        "&6/plots destroy:&r Destroys all plot protection on the current plot you are standing on",
                };
                sender.sendMessage(ChatColor.DARK_AQUA + "" +  ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                                    + ChatColor.AQUA
                        + " Displaying help/ all commands for plots.");
                for (String s : help) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                }
                if(sender.hasPermission("plots.admin")){
                    for (String s : adminHelp) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                    }
                }
            } else if (args[0].equalsIgnoreCase("select")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED
                            + "Please use the correct syntax: /plots select [1 or 2]");
                } else {
                    if (args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("2")) {
                        Location l = sender.getLocation();
                        sender.sendMessage(ChatColor.DARK_AQUA + "" +  ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                                    + ChatColor.AQUA
                                + " Selected corner number " + args[1] + " @(" + l.getBlockX() + ", "
                                + l.getBlockY() + ", " + l.getBlockZ() + ")");
                        corners[(Integer.parseInt(args[1])) - 1] = l;
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RESET + ChatColor.RED
                                + "Please use the correct syntax: /plots select [1 or 2]");
                    }
                }
            } else if (args[0].equalsIgnoreCase("claim")) {
                ArrayList<ArrayList<String>> results = sql.getQuery("SELECT * FROM plots");
                Integer plotId = results.size()+1;
                sql.query("INSERT INTO plots (Owner,corner1x,corner1y,corner1z,corner2x,corner2y,corner2z) " +
                        "VALUES ('" + sender.getName() + "','" +
                        corners[0].getBlockX() + "','" +
                        corners[0].getBlockY() + "','" +
                        corners[0].getBlockZ() + "','" +
                        corners[1].getBlockX() + "','" +
                        corners[1].getBlockY() + "','" +
                        corners[1].getBlockZ() + "');");
                sql.query("CREATE TABLE `plot_" + plotId + "` (" +
                        "`ID`INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "`username`TEXT," +
                        "`rank`TEXT," +
                        "`addedBy`TEXT" +
                        ");");

                sender.sendMessage(ChatColor.DARK_AQUA + "" +  ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                                    + ChatColor.AQUA + " Successfully " + ChatColor.BOLD
                        + "claimed " + ChatColor.RESET + ChatColor.AQUA + area.getColumnCount(corners[0], corners[1])
                        + " columns under plot "
                        + Integer.toString(results.size() + 1) + ". Type /plots options for all plot actions.");
            } else if (args[0].equalsIgnoreCase("add")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED
                            + "Please use the correct syntax: /plots add [username]");
                } else if(!plots.inAnyPlot(sender.getLocation())) {
                    sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED
                            + "Outside of a plot. Please run this command standing anywhere on the plot.");
                }
                else
                {
                    String plotId = Integer.toString(plots.getPlotId(sender.getLocation()));
                    sql.query("INSERT INTO plot_" + plotId +
                            " (username, rank, addedBy) VALUES('" + args[1] +
                            "', 'member', '" + sender.getName() + "')");
                    sender.sendMessage(ChatColor.DARK_AQUA + "" +  ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                                    + ChatColor.AQUA
                            + " Successfully " + ChatColor.BOLD + " added " + ChatColor.RESET + ChatColor.AQUA + args[1]
                            + " to plot " + (plotId) + ". To see all people added to this plot type /plots users.");

                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED
                            + "Please use the correct syntax: /plots removeOwner [username]");
                } else if(!plots.inAnyPlot(sender.getLocation())) {
                    sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED
                            + "Outside of a plot. Please run this command standing anywhere on the plot.");
                } else if (plots.inAnyPlot(sender.getLocation())) {
                    Integer plotId = plots.getPlotId(sender.getLocation());
                    ArrayList<ArrayList<String>> users = plots.getPlotUsers(plotId);
                    if(users == null){
                        users = new ArrayList<ArrayList<String>>();
                    }
                    for(ArrayList<String> row: users){
                        if(row.get(1).equalsIgnoreCase(args[1])){
                            sql.query("DELETE FROM plot_" + plotId + " WHERE username = '" + args[1] + "'");
                            sender.sendMessage(ChatColor.DARK_AQUA + "" +  ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                                    + ChatColor.AQUA
                                    + " Successfully " + ChatColor.BOLD + "removed " + ChatColor.RESET + ChatColor.AQUA
                                    + args[1] + " from plot " + (plotId + 1)
                                    + ". To see all members of this plot type /plots users.");
                            break commandLoop;
                        }
                    }
                    sender.sendMessage(ChatColor.DARK_AQUA + "" +  ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                                    + ChatColor.AQUA
                            + " Sorry, but there is no player currently on this plot with the name " + args[1]
                            + ". To see all members of this plot type /plots users.");
                }
            } else if (args[0].equalsIgnoreCase("lookup")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED
                            + "Please use the correct syntax: /plots lookup [plotID or name]");
                } else {
                    ArrayList<String> plot = sql.getQuery("SELECT * FROM plots WHERE ID = " + args[1]
                            + " OR name = " + args[1]).get(0);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&6Plot ID:&r " + plot.get(0) + "\n&6Plot name:&r " + plot.get(8)
                                    + "\n&6Plot owner:&r " + plot.get(1)
                    ));
                }
            } else if (args[0].equalsIgnoreCase("addOwner")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED
                            + "Please use the correct syntax: /plots add [username]");
                } else if(!plots.inAnyPlot(sender.getLocation())) {
                    sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED
                            + "Outside of a plot. Please run this command standing anywhere on the plot.");
                }
                else {
                    Integer plotId = plots.getPlotId(sender.getLocation());
                    if (plots.getPlotUser(plotId, args[1]) == null) {
                        sql.query("INSERT INTO plot_" + plotId +
                                " (username, rank, addedBy) VALUES('" + args[1] +
                                "', 'owner', '" + sender.getName() + "')");
                        sender.sendMessage(ChatColor.DARK_AQUA + "" +  ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                                    + ChatColor.AQUA
                                + " Successfully " + ChatColor.BOLD + "added " + ChatColor.RESET + ChatColor.AQUA
                                +  args[1] + " as an owner to plot " + (plotId)
                                + ". To see all people added to this plot type /plots users.");
                    } else {
                        sql.query("UPDATE plot_" + plotId + " SET rank = 'owner' WHERE username = '" + args[1] + "'");
                        sender.sendMessage(ChatColor.DARK_AQUA + "" +  ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                                    + ChatColor.AQUA
                                + " Successfully " + ChatColor.BOLD + "promoted " + ChatColor.RESET + ChatColor.AQUA
                                + args[1] + " to plot owner on plot " + plotId
                                + ". To see all people added to this plot type /plots users.");
                    }
                }
            } else if (args[0].equalsIgnoreCase("removeOwner")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED
                            + "Please use the correct syntax: /plots removeOwner [username]");
                } else if(!plots.inAnyPlot(sender.getLocation())) {
                    sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED
                            + "Outside of a plot. Please run this command standing anywhere on the plot.");
                } else if (plots.inAnyPlot(sender.getLocation())) {
                    Integer plotId = plots.getPlotId(sender.getLocation());
                    ArrayList<ArrayList<String>> users = plots.getPlotUsers(plotId);
                    if(users == null){
                        users = new ArrayList<ArrayList<String>>();
                    }
                    for(ArrayList<String> row: users){
                        if(row.get(1).equalsIgnoreCase(args[1])){
                            sql.query("DELETE FROM plot_" + plotId + " WHERE username = '" + args[1] + "'");
                            sender.sendMessage(ChatColor.DARK_AQUA + "" +  ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                                    + ChatColor.AQUA
                                    + " Successfully " + ChatColor.BOLD + "removed " + ChatColor.RESET + ChatColor.AQUA
                                    + args[1] + " from plot " + (plotId + 1)
                                    + ". To see all members of this plot type /plots users.");
                            break commandLoop;
                        }
                    }
                    sender.sendMessage(ChatColor.DARK_AQUA + "" +  ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                                    + ChatColor.AQUA
                            + " Sorry, but there is no player currently on this plot with the name " + args[1]
                            + ". To see all members of this plot type /plots users.");
                }
            } else if (args[0].equalsIgnoreCase("users")) {
                if(!plots.inAnyPlot(sender.getLocation())) {
                    sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED
                            + "Outside of a plot. Please run this command standing anywhere on the plot.");
                } else  if (plots.inAnyPlot(sender.getLocation())) {
                    ArrayList<ArrayList<String>> plotUsers = plots.getPlotUsers(plots.getPlotId(sender.getLocation()));
                    String users = "";
                    if (!(plotUsers == null)){
                        for (ArrayList<String> u : plotUsers) {
                            if (u.get(2).equalsIgnoreCase("owner")) {
                                users += "(Co-Owner)" + u.get(1) + ", ";
                            } else {
                                users += u.get(1) + ", ";
                            }
                        }
                    }
                    else {
                        users = "(None)";
                    }
                    sender.sendMessage(ChatColor.DARK_AQUA + "" +  ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                                    + ChatColor.AQUA
                            + " Showing all users for plot " + plots.getPlotId(sender.getLocation())
                            + ChatColor.translateAlternateColorCodes('&', "\n&6Plot Owner:&r "
                            + plots.getOwner(plots.getPlotId(sender.getLocation()))
                            + "\n&6Plot Users:&r " + users));
                }
            }  else if (args[0].equalsIgnoreCase("options")) {
                if (plots.inAnyPlot(sender.getLocation())) {
                    if(plots.hasRank(sender, plots.getPlotId(sender.getLocation()), "owner")||
                            plots.getOwner(plots.getPlotId(sender.getLocation())).equalsIgnoreCase(sender.getName())) {
                        String[] help = {
                                "&6/plots sell:&r Sells the current plot you are standing on if you are the owner.",
                                "&6/plots add [player]:&r Adds a player to the current plot you are standing on.",
                                "&6/plots remove [player]:&r Removes a player from the current plot you are standing on.",
                                "&6/plots name [name]:&r Assigns a name to the current plot you are standing on.",
                        };
                        String[] adminHelp = {
                                "&6/plots addOwner [player]:&r Adds a player and grants them full ownership permissions of the plot to the current plot you are standing on.",
                                "&6/plots removeOwner [player]:&r Removes a player  and strips them of any permissions related to the current plot are standing on.",
                                "&6/plots lock [lock type]:&r Locks a plot to everyone but the claimer, any owners or anyone with permission(based on lock type).",
                                "&6/plots unlock:&r Removes any current lock from the current plot you are standing on.",
                                "&6/plots destroy:&r Destroys all plot protection on the current plot you are standing on",
                        };
                        sender.sendMessage(ChatColor.DARK_AQUA + "" +  ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                                    + ChatColor.AQUA
                                + " Displaying all commands for use on this plot.");
                        for (String s : help) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                        }
                        if (sender.hasPermission("plots.admin")) {
                            for (String s : adminHelp) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.DARK_AQUA + "" +  ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                                    + ChatColor.AQUA
                                + " There are no commands available to you on this plot at the current time.");
                    }

                } else if(!plots.inAnyPlot(sender.getLocation())) {
                    sender.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED
                            + "Outside of a plot. Please run this command standing anywhere on the plot.");
                }
            } 
        }
        return true;
    }
}
