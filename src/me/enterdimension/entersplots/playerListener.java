package me.enterdimension.entersplots;

import me.enterdimension.entersplots.inc.plots;
import me.enterdimension.entersplots.inc.sql;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

/*

    Produced by Isaac Brown
    Last edited 11/01/2015 by Isaac

    Copyright 2015 Enters-Domain, All Rights Reserved

    Website: http://www.enters-domain.com
    License: http://www.enters-domain.com/license

*/
public class playerListener implements Listener {

    public playerListener(main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        sql.log = false;
        Player player = e.getPlayer();
        Block block = e.getBlock();
        if(plots.inAnyPlot(block.getLocation())) {
            Integer plotId = plots.getPlotId(block.getLocation());
            if (!(plots.hasRank(player, plotId, "owner") ||
                    plots.hasRank(player, plotId, "member") ||
                    player.hasPermission("plots.override"))) {
                player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                        + ChatColor.AQUA
                        + " Sorry! You do not have permission to build on this plot ("
                        + plots.getPlotName(plotId) + ").");
                e.setCancelled(true);
            }
        }
        sql.log = true;
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        sql.log = false;
        Player player = e.getPlayer();
        Block block = e.getBlock();
        if(plots.inAnyPlot(block.getLocation())) {
            Integer plotId = plots.getPlotId(block.getLocation());
            if (!(plots.hasRank(player, plotId, "owner") ||
                    plots.hasRank(player, plotId, "member") ||
                    player.hasPermission("plots.override"))) {
                player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                        + ChatColor.AQUA
                        + " Sorry! You do not have permission to mine on this plot ("
                        + plots.getPlotName(plotId) + ").");
                e.setCancelled(true);
            }
        }
        sql.log = true;
    }
    @EventHandler
    public void onMelee(EntityDamageByEntityEvent e){
        sql.log = false;
        if(e.getDamager() instanceof Player) {
            if(e.getEntity() instanceof Skeleton || e.getEntity() instanceof Ghast){
                return;
            }
            Player player = (Player) e.getDamager();
            Location location = e.getEntity().getLocation();
            if (plots.inAnyPlot(location)) {
                Integer plotId = plots.getPlotId(location);
                if (!(plots.hasRank(player, plotId, "owner") ||
                        plots.hasRank(player, plots.getPlotId(location), "member") ||
                        player.hasPermission("plots.override"))) {
                    player.sendMessage(ChatColor.DARK_AQUA + "" +  ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                            + ChatColor.AQUA
                            + " Sorry! You do not have permission to attack entities (with the exception of skeletons)"
                            + " on this plot (" + plots.getPlotName(plots.getPlotId(location)) + ").");
                    e.setCancelled(true);
                }
            }
        }
        sql.log = true;
    }

    @EventHandler

    public void onMove(PlayerMoveEvent e) {
        sql.log = false;
        Player player = e.getPlayer();
        Location location = player.getLocation();
        Location from = e.getFrom();
        Location to = e.getTo();
        Integer plotId;
        Boolean movedPlace = from.getBlockX() != to.getBlockX() ||
                from.getBlockZ() != to.getBlockZ();
        inLockedPlot:
        if (plots.inAnyPlot(to)) {
            plotId = plots.getPlotId(location);
            if (!plots.getLockType(plotId).equalsIgnoreCase("none") && !player.hasPermission("plots.override")) {
                if (plots.getLock(plotId).equalsIgnoreCase("password")) {
                    if (main.rePassword.containsKey(player)) {
                        if (main.rePassword.get(player)
                                .equalsIgnoreCase(plots.getLock(plotId))) {
                            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                                    + ChatColor.AQUA
                                    + " You can now access " + plots.getPlotName(plotId)
                                    + ". You will be able to access this plot until you leave the server.");
                            plots.allowAccess(player, plotId);
                            break inLockedPlot;
                        } else {
                            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                                    + ChatColor.AQUA
                                    + " Incorrect password. Please try again or contact an admin if you have "
                                    + "forgotten your password");
                        }
                    } else {
                        player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                                + ChatColor.AQUA
                                + " In order to access this plot(" + plots.getPlotName(plotId)
                                + ") you must enter a password . Please type /plots password [password]");
                        main.promptPassword.put(player, true);
                        return;
                    }
                    e.setTo(from);
                    player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                            + ChatColor.AQUA
                            + " Sorry! You do not have permission to walk on this plot ("
                            + plots.getPlotName(plots.getPlotId(location)) + ").");
                }
            }

            if (plots.inAnyPlot(to) && !plots.inAnyPlot(from)) {
                plotId = plots.getPlotId(to);
                String suf = plots.getOwner(plotId).substring(plots.getOwner(plotId).length() - 1).equalsIgnoreCase("s")
                        ? "' " : "'s";
                player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                        + ChatColor.AQUA
                        + " Now " + ChatColor.BOLD + "entering " + ChatColor.RESET + ChatColor.AQUA
                        + plots.getPlotName(plotId) + " (" + plots.getOwner(plotId) + suf + " plot)");
            } else if (!plots.inAnyPlot(to) && plots.inAnyPlot(from)) {
                plotId = plots.getPlotId(from);
                String suf = plots.getOwner(plotId).substring(plots.getOwner(plotId).length() - 1).equalsIgnoreCase("s")
                        ? "' " : "'s";
                player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "[Plots]" + ChatColor.RESET
                        + ChatColor.AQUA
                        + " Now " + ChatColor.BOLD + "leaving " + ChatColor.RESET + ChatColor.AQUA
                        + plots.getPlotName(plotId) + " (" + plots.getOwner(plotId) + suf + " plot)");
            }
        }
        sql.log = true;
    }

}
