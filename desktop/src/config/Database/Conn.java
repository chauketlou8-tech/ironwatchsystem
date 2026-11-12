package config.Database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conn {
    public static Connection getConnection() throws IOException, SQLException {
        Properties properties = new Properties();

        try (InputStream inputStream = Conn.class.getClassLoader().getResourceAsStream("config/application.properties")) {

            if (inputStream == null) {
                throw new RuntimeException("config/application.properties not found");
            }

            properties.load(inputStream);
        }

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        return DriverManager.getConnection(url, username, password);
    }
}
