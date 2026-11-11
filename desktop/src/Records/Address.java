package Records;

import java.sql.Timestamp;

public record Address(long id, String street, String city, String province, String postalCode, String country, Timestamp created_at,  Timestamp updated_at) {
}
