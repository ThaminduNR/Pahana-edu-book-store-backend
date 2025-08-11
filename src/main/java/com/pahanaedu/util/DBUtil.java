package com.pahanaedu.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    private static final String URL  = "jdbc:mysql://localhost:3308/pahanaedu";
    private static final String USER = "root";
    private static final String PASS = "1234"; // change as needed

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // load driver
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    public static Connection getConnection() throws Exception {
        System.out.println("Connecting to DB: " + URL + " as " + USER);
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
