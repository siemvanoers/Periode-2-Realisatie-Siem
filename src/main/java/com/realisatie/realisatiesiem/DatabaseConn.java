package com.realisatie.realisatiesiem;

import java.sql.*;
import java.util.Properties;

public class DatabaseConn {
    // Instance variables
    private Connection connection;
    private final Properties properties;

    // Constructor
    public DatabaseConn(String hostname, String port, String database, String username, String password) {
        this.properties = new Properties();
        this.properties.setProperty("hostname", hostname);
        this.properties.setProperty("port", port);
        this.properties.setProperty("database", database);
        this.properties.setProperty("user", username);
        this.properties.setProperty("password", password);
    }

    // Method to establish a database connection
    private void connect() {
        String url = "jdbc:mysql://%s:%s/%s".formatted(
                this.properties.getProperty("hostname"),
                this.properties.getProperty("port"),
                this.properties.getProperty("database")
        );

        try {
            this.connection = DriverManager.getConnection(url, this.properties);
        } catch (SQLException ex) {
            this.connection = null;
            System.out.println("An error occurred while connecting to the MySQL database");
            System.out.println(ex.getMessage());
        }
    }

    // Method to get the database connection
    public Connection getConnection() {
        if (this.isConnected()) {
            this.connect();
        }
        return this.connection;
    }

    // Method to check if the connection is established
    public boolean isConnected() {
        try {
            return this.connection == null || this.connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    // Method to execute a SQL query
    public ResultSet query(String query) throws SQLException {
        if (this.connection == null) {
            this.connect();
        }
        Statement statement = this.connection.createStatement();
        return statement.executeQuery(query);
    }
}
