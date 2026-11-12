package Models;

import java.io.Serializable;
import java.sql.Timestamp;

public record User(long id, String firstName, String lastName, String email, String password, String position,
                   String profile_picture_path, Timestamp created_at, Timestamp updated_at) implements Serializable {
    public User(long id, String firstName, String lastName, String email, String password, String position, String profile_picture_path, Timestamp created_at, Timestamp updated_at) {
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
}
