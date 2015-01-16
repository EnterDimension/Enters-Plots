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
    public static Boolean inArea(Location corner1, Location corner2, Location location) {
        return (((location.getBlockX() >= corner1.getBlockX() && location.getBlockX() <= corner2.getBlockX())
                || (corner1.getBlockX() >= location.getBlockX() && corner2.getBlockX() <= location.getBlockX()))
                && ((location.getBlockZ() >= corner1.getBlockZ() && location.getBlockZ() <= corner2.getBlockZ())
                || (corner1.getBlockZ() >= location.getBlockZ() && corner2.getBlockZ() <= location.getBlockZ())));
    }
    public static Boolean inBorder(Location corner1, Location corner2, Location location) {
        return (location.getBlockX() >= corner1.getBlockX() && location.getBlockX() <= corner2.getBlockX())
                && (location.getBlockZ() == corner1.getBlockZ()||location.getBlockZ() == corner2.getBlockZ())
                ||(location.getBlockZ() >= corner1.getBlockZ() && location.getBlockZ() <= corner2.getBlockZ())
                && (location.getBlockX() == corner1.getBlockX()||location.getBlockX() == corner2.getBlockX());
    }
    public static Integer getColumnCount(Location corner1, Location corner2){
        return (Math.abs(corner1.getBlockX() - corner2.getBlockX()))
                * (Math.abs(corner1.getBlockZ() - corner2.getBlockZ()));
    }

}
