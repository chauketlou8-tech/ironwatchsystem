package Models;

import Enums.Gender;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String position;
    private final String profile_picture_path;
    private final Timestamp created_at;
    private final Timestamp updated_at;

    public  User(long id, String firstName, String lastName, String email, String password, String position, String profile_picture_path, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.position = position;
        this.profile_picture_path = profile_picture_path;
        this.created_at = new Timestamp(created_at.getTime());
        this.updated_at = new Timestamp(updated_at.getTime());
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPosition() {
        return position;
    }

    public String getProfile_picture_path() {
        return profile_picture_path;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }
}
