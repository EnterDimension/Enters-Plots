package me.enterdimension.entersplots.inc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getLogger;

/*
    
    Produced by Isaac Bplotn
    Last edited 11/01/2015 by Isaac
    
    Copyright 2015 Enters-Domain, All Rights Reserved
    
    Website: http://www.enters-domain.com
    License: http://www.enters-domain.com/license

*/
public class plots {
    public static ArrayList<String> getPlot(Integer plotId){
        ArrayList<ArrayList<String>> results = sql.getQuery("SELECT * FROM plots WHERE ID=" + plotId);
        if(results.size() == 1)return results.get(0);
        getLogger().warning("Found multiple or nil results for plot ID "+plotId+". Canceling process.");
        return null;
    }
    private static ArrayList<String> getPlotUsers(Integer plotId){
        ArrayList<ArrayList<String>> results = sql.getQuery("SELECT * FROM plots_" + plotId);
        if(results.size() == 1)return results.get(0);
        getLogger().warning("Found multiple or nil results for plot ID "+plotId+". Canceling process.");
        return null;

    }
    public static boolean inPlot(Player player, Integer plotId) {

        ArrayList<String> plot = getPlot(plotId);
        Location corner1 = new Location(player.getWorld(),
                Double.parseDouble(plot.get(2)),Double.parseDouble(plot.get(3)),Double.parseDouble(plot.get(4)));
        Location corner2 = new Location(player.getWorld(),
                Double.parseDouble(plot.get(5)),Double.parseDouble(plot.get(6)),Double.parseDouble(plot.get(7)));
        return area.inArea(corner1, corner2, player.getLocation());
    }
    public static boolean inAnyPlot(Player player){
        ArrayList<ArrayList<String>> results = sql.getQuery("SELECT * FROM plots");
        for(ArrayList<String> plot:results){
            if(inPlot(player, Integer.parseInt(plot.get(0)))) return true;
        }
        return false;
    }
    public static Integer getPlotId(Location location){
        ArrayList<ArrayList<String>> results = sql.getQuery("SELECT * FROM plots");
        for(ArrayList<String> plot:results){
            Location corner1 = new Location(location.getWorld(),
                    Double.parseDouble(plot.get(2)),Double.parseDouble(plot.get(3)),Double.parseDouble(plot.get(4)));
            Location corner2 = new Location(location.getWorld(),
                    Double.parseDouble(plot.get(5)),Double.parseDouble(plot.get(6)),Double.parseDouble(plot.get(7)));
            if(area.inArea(corner1,corner2,location)) return Integer.parseInt(plot.get(0));
        }
        return null;
    }
    public static boolean hasRank(Player player,  Integer plotId, String permission){
        ArrayList<String> plot = sql.getQuery("SELECT * FROM plot_" + plotId + "WHERE username = "
                + player.getName()).get(0);
        return plot.get(2).equalsIgnoreCase(permission);
    }
}