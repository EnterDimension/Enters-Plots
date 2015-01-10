package me.enterdimension.entersplugin.inc;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
/**
 * Created by Isaac on 10/01/2015.
 *
 */
public class area {
    public static Boolean inArea(World world,Location corner1,Location corner2,Location ply){
        return (ply.getBlockX() >= corner1.getBlockX() && ply.getBlockX() <= corner2.getBlockX())||
                (ply.getBlockZ() >= corner1.getBlockZ() && ply.getBlockZ() <= corner2.getBlockZ());
    }
}
