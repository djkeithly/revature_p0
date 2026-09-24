package com.revature.persistence;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.exception.DatabaseConnectionException;

public class ConnectionFactory {
    
    private static final ConnectionFactory connectionFactory = new ConnectionFactory();
    private Properties props = new Properties();

    private ConnectionFactory() {
        try {
            props.load(new FileReader("src/main/resources/db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    props.getProperty("DB_URL"),
                    props.getProperty("DB_USERNAME"),
                    props.getProperty("DB_PASSWORD"));
        } catch (SQLException e) {
            // We don't need to create a logger every time we make a connection, we just need a logger to tell us when the database is down.
            Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);
            logger.error("Connection to database down");
            throw new DatabaseConnectionException("Connection to service lost");
        }
    }

    
}