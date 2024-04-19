package com.example.test1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/freelance";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static DBConnection instance;
    private Connection cnx;

    private DBConnection() {
        try {
            this.cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected To DATABASE !");
        } catch (SQLException var2) {
            System.err.println("Error: " + var2.getMessage());
        }

    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }

        return instance;
    }

    public Connection getCnx() {
        return this.cnx;
    }
}
