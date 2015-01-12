package me.enterdimension.entersplots.inc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/*
    
    Produced by Isaac Brown
    Last edited 11/01/2015 by Isaac
    
    Copyright 2015 Enters-Domain, All Rights Reserved
    
    Website: http://www.enters-domain.com
    License: http://www.enters-domain.com/license

*/
public class plots {
    public static boolean inPlot(Player player,Integer plotId) {
        ArrayList<ArrayList<String>> results = sql.getQuery("SELECT * FROM plots WHERE ID=" + plotId);
        ArrayList<String> row = results.get(0);
        Location corner1 = new Location(Bukkit.getServer().getWorld("world"),
                Double.parseDouble(row.get(2)),Double.parseDouble(row.get(3)),Double.parseDouble(row.get(4)));
        Location corner2 = new Location(Bukkit.getServer().getWorld("world"),
                Double.parseDouble(row.get(5)),Double.parseDouble(row.get(6)),Double.parseDouble(row.get(7)));
        return area.inArea(corner1, corner2, player.getLocation());
    }
    public static boolean inAnyPlot(Player player){
        ArrayList<ArrayList<String>> results = sql.getQuery("SELECT * FROM plots");
        for(ArrayList<String> row:results){
            if(inPlot(player, Integer.parseInt(row.get(0)))) return true;
        }
        return false;
    }
    public static Integer getPlot(Location location){
        ArrayList<ArrayList<String>> results = sql.getQuery("SELECT * FROM plots");
        for(ArrayList<String> row:results){
            Location corner1 = new Location(Bukkit.getServer().getWorld("world"),
                    Double.parseDouble(row.get(2)),Double.parseDouble(row.get(3)),Double.parseDouble(row.get(4)));
            Location corner2 = new Location(Bukkit.getServer().getWorld("world"),
                    Double.parseDouble(row.get(5)),Double.parseDouble(row.get(6)),Double.parseDouble(row.get(7)));
            if(area.inArea(corner1,corner2,location)) return Integer.parseInt(row.get(0));
        }
        return null;
    }
}
