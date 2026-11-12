package Repositories;

import Models.User;
import config.Database.Conn;
import sockets.Client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ServerRepository {
    public ServerRepository(Consumer<BufferedImage> stream) throws IOException {
        Client client = new Client(stream);
    }

    public static List<User> getUsers() throws IOException, SQLException {
        List<User> users = new ArrayList<>();
        Connection connection = Conn.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("select * from users")) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                users.add(new User(
                        rs.getLong("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("position"),
                        rs.getString("profile_picture_path"),
                        (Timestamp)  rs.getObject("created_at"),
                        (Timestamp) rs.getObject("updated_at")
                ));
            }
        }

        return users;
    }
}
