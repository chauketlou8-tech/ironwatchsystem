package Repositories;

import Enums.Gender;
import Models.Employee;
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
    static Connection connection;

    static {
        try {
            connection = Conn.getConnection();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerRepository(Consumer<BufferedImage> stream) throws IOException {
        Client client = new Client(stream);
    }

    public static List<User> getUsers() throws SQLException {
        List<User> users = new ArrayList<>();

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

    public static List<Employee> getEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("select * from employees")) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                employees.add(new Employee(
                        rs.getLong("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        (Date) rs.getObject("date_of_birth"),
                        rs.getDouble("salary"),
                        (Date) rs.getObject("hire_date"),
                        rs.getString("position"),
                        rs.getString("department"),
                        rs.getString("phone_number"),
                        Gender.valueOf(rs.getString("gender")),
                        rs.getInt("address_id"),
                        rs.getString("profile_picture_path"),
                        (Timestamp) rs.getObject("created_at"),
                        (Timestamp) rs.getObject("updated_at")
                ));
            }
        }

        return employees;
    }
}
