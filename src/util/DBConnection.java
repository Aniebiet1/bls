package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Postgres driver not found on classpath: " + e.getMessage());
        }
    }

    public static Connection get() throws SQLException {
        String url = AppConfig.getOrDefault("db.url", "jdbc:postgresql://localhost:5432/bls");
        String user = AppConfig.getOrDefault("db.user", "bls_user");
        String pass = AppConfig.getOrDefault("db.password", "hello");
        return DriverManager.getConnection(url, user, pass);
    }
}
