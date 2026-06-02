package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;


    private final String URL = "jdbc:mysql://localhost:3306/ghads_final_project_db";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("تم الاتصال بقاعدة البيانات بنجاح!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("فشل الاتصال بقاعدة البيانات!");
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}