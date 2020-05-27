package kea.motorhome.motorhomesite.util;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DBConnectionManager
{
    private static String user;
    private static String password;
    private static String url;

    private static Connection connection;

    private DBConnectionManager(){}

    public static Connection getConnection()
    {
        if(connection != null){return connection;}
        return connection = getDatabaseConnection();
    }

    private static Connection getDatabaseConnection()
    {
        Properties prop = new Properties();
        try
        {
            FileInputStream propertyFile = new FileInputStream("src/main/resources/application.properties");
            prop.load(propertyFile);
            user = prop.getProperty("db.user");
            password = prop.getProperty("db.password");
            url = prop.getProperty("db.url");
        } catch(FileNotFoundException e)
        {
            System.out.println("File could not be found");
            e.printStackTrace();
        } catch(IOException e)
        {
            System.out.println("Property could not be loaded");
            e.printStackTrace();
        }
        try
        {
            return DriverManager.getConnection(url, user, password);
        } catch(SQLException e)
        {
            System.out.println("SQL Exception while establishing connection.");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Method changes the default user, password and url. Connection is attempted,
     * and the Singleton instance will return the new Connection.
     * Returns true if now conenction is successful
     */
    public static boolean set(String USER, String PASSWORD, String URL)
    {
        user = USER;
        password = PASSWORD;
        url = URL;

        if(getDatabaseConnection()!=null){
            return true;
        }
        return false;
    }
}