package me.enterdimension.entersplots;

import me.enterdimension.entersplots.inc.area;
import me.enterdimension.entersplots.inc.plots;
import me.enterdimension.entersplots.inc.sql;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

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
        Player player = e.getPlayer();
        Block block = e.getBlock();
        if(plots.inAnyPlot(block.getLocation())) if (!(plots.hasRank(player, plots.getPlotId(block.getLocation()), "owner") ||
                plots.hasRank(player, plots.getPlotId(block.getLocation()), "member") ||
                player.hasPermission("plots.override"))) {
            player.sendMessage(ChatColor.DARK_AQUA + "[Plots]" + ChatColor.AQUA
                    + " Sorry! You do not have permission to build on this plot ("
                    + plots.getPlotName(plots.getPlotId(block.getLocation())) + ").");
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        Block block = e.getBlock();
        if(plots.inAnyPlot(block.getLocation())) if (!(plots.hasRank(player, plots.getPlotId(block.getLocation()), "owner") ||
                plots.hasRank(player, plots.getPlotId(block.getLocation()), "member") ||
                player.hasPermission("plots.override"))) {
            player.sendMessage(ChatColor.DARK_AQUA + "[Plots]" + ChatColor.AQUA
                    + " Sorry! You do not have permission to mine on this plot ("
                    + plots.getPlotName(plots.getPlotId(block.getLocation())) + ").");
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onMelee(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player) {
            if(e.getEntity() instanceof Skeleton || e.getEntity() instanceof Ghast){
                return;
            }
            Player player = (Player) e.getDamager();
            Location location = e.getEntity().getLocation();
            if (plots.inAnyPlot(location))
                if (!(plots.hasRank(player, plots.getPlotId(location), "owner") ||
                        plots.hasRank(player, plots.getPlotId(location), "member") ||
                        player.hasPermission("plots.override"))) {
                    player.sendMessage(ChatColor.DARK_AQUA + "[Plots]" + ChatColor.AQUA
                            + " Sorry! You do not have permission to attack entities (with the exception of skeletons)"
                            + " on this plot (" + plots.getPlotName(plots.getPlotId(location)) + ").");
                    e.setCancelled(true);
                }
        }
    }
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location location = player.getLocation();
        Integer plotId = plots.getPlotId(location);
        ArrayList<String> plot = plots.getPlot(plotId);
        Location corner1 = new Location(location.getWorld(),
                Double.parseDouble(plot.get(2)),Double.parseDouble(plot.get(3)),Double.parseDouble(plot.get(4)));
        Location corner2 = new Location(location.getWorld(),
                Double.parseDouble(plot.get(5)),Double.parseDouble(plot.get(6)),Double.parseDouble(plot.get(7)));
        if(plots.inAnyPlot(location)) if (!plots.getLockType(plotId).equalsIgnoreCase("none")
                && player.hasPermission("plots.override")) {
            player.sendMessage(ChatColor.DARK_AQUA + "[Plots]" + ChatColor.AQUA
                    + " Sorry! You do not have permission to walk on this plot ("
                    + plots.getPlotName(plots.getPlotId(location)) + ").");
            Location from = e.getFrom();
            Location to = e.getTo();
            if(from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
                e.setTo(from);
            }
        } 
    }
}
