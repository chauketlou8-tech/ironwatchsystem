package Models;

import Enums.Gender;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Employee implements Serializable {

    private final long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Date birthDate;
    private final double salary;
    private final Date hireDate;
    private final String position;
    private final String department;
    private final String phoneNumber;
    private final Gender gender;
    private final int addressId;
    private final String profile_picture_path;
    private final Timestamp created_at;
    private final Timestamp updated_at;

    public Employee(long id, String firstName, String lastName, String email,  Date birthDate, double salary, Date hireDate, String position, String department, String phoneNumber, Gender gender, int addressId, String profile_picture_path, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = new Date(birthDate.getTime());
        this.salary = salary;
        this.hireDate = new Date(hireDate.getTime());
        this.position = position;
        this.department = department;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.addressId = addressId;
        this.profile_picture_path = profile_picture_path;
        this.created_at = new  Timestamp(created_at.getTime());
        this.updated_at = new  Timestamp(updated_at.getTime());
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

    public Date getBirthDate() {
        return birthDate;
    }

    public double getSalary() {
        return salary;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public String getPosition() {
        return position;
    }

    public String getDepartment() {
        return department;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public int getAddressId() {
        return addressId;
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
