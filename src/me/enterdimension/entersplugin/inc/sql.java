package me.enterdimension.entersplugin.inc;

import java.sql.*;
import java.util.ArrayList;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.resetRecipes;

/**
 * Created by Isaac on 10/01/2015.
 *
 */
public class sql {

    public static void connect() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:plugins/plots.db");
            getLogger().info("[Enters Plugin]Opened database successfully");
            c.close();
        } catch ( Exception e ) {
            getLogger().warning(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        getLogger().info("[Enters Plugin]SQL connection closed");
    }

    public static void query(String query){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:plugins/plots.db");
            getLogger().info("[Enters Plugin]Opened database successfully");
            stmt = c.createStatement();
            stmt.executeUpdate(query);
            stmt.close();

            c.close();
        } catch ( Exception e ) {
            getLogger().warning("[Enters Plugin]SQL error - "+e.getClass().getName() + ": " + e.getMessage());
        }
        getLogger().info("[Enters Plugin]SQL connection closed");
    }
    public static ArrayList<ArrayList<String>> getQuery(String query){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:plugins/plots.db");
            getLogger().info("[Enters Plugin]Opened database successfully");
            stmt = c.createStatement();
            ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
            ResultSet resultset =  stmt.executeQuery(query );
            while(resultset.next()) {
                ArrayList<String> row = new ArrayList<String>();
                for (int i = 1; i < resultset.getMetaData().getColumnCount() + 1; i++ ) {
                    row.add(resultset.getString(i));
                }
                results.add(row);
            }
            resultset.close();
            stmt.close();
            stmt.close();
            c.close();
            getLogger().info("[Enters Plugin]SQL connection closed");
            return results;
        } catch ( Exception e ) {
            getLogger().warning("[Enters Plugin]SQL error - "+e.getClass().getName() + ": " + e.getMessage());
            getLogger().info("[Enters Plugin]SQL connection closed");
            return null;

        }



    }
}
