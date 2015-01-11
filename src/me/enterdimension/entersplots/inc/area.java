package me.enterdimension.entersplots.inc;

import org.bukkit.Location;

/*

    Produced by Isaac Brown
    Last edited 11/01/2015 by Isaac

    Copyright 2015 Enters-Domain, All Rights Reserved

    Website: http://www.enters-domain.com
    License: http://www.enters-domain.com/license

*/

public class area {
    public static Boolean inArea(Location corner1, Location corner2, Location ply) {
        return (((ply.getBlockX() >= corner1.getBlockX() && ply.getBlockX() <= corner2.getBlockX())
                || (corner1.getBlockX() >= ply.getBlockX() && corner2.getBlockX() <= ply.getBlockX()))
                && ((ply.getBlockZ() >= corner1.getBlockZ() && ply.getBlockZ() <= corner2.getBlockZ())
                || (corner1.getBlockZ() >= ply.getBlockZ() && corner2.getBlockZ() <= ply.getBlockZ())));
    }
    public static Integer getColumnCount(Location corner1, Location corner2){
        return (Math.abs(corner1.getBlockX() - corner2.getBlockX()))
                * (Math.abs(corner1.getBlockZ() - corner2.getBlockZ()));
    }
}
