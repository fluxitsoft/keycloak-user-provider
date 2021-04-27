package com.fluxit.demo.auth.provider.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.keycloak.component.ComponentModel;


public class DbUtil {

    private static final String CONFIG_KEY_JDBC_DRIVER = "config.key.jdbc.driver";
    private static final String CONFIG_KEY_JDBC_URL = "config.key.jdbc.url";
    private static final String CONFIG_KEY_DB_USERNAME = "config.key.db.username";
    private static final String CONFIG_KEY_DB_PASSWORD = "config.key.db.password";
	
	public static Connection getConnection(ComponentModel config) throws SQLException{
        String driverClass = config.get(CONFIG_KEY_JDBC_DRIVER);
        try {
            Class.forName(driverClass);
        }
        catch(ClassNotFoundException nfe) {
            throw new RuntimeException("Invalid JDBC driver: " + driverClass + ". Please check if your driver if properly installed");
        }
        
        return DriverManager.getConnection(config.get(CONFIG_KEY_JDBC_URL),
          config.get(CONFIG_KEY_DB_USERNAME),
          config.get(CONFIG_KEY_DB_PASSWORD));
    }
}
