package com.epam.alextuleninov.taxiservice.data.user;

import com.epam.alextuleninov.taxiservice.model.user.User;

/**
 * Class for the UserResponse.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class UserResponse {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String role;

    public UserResponse(long id, String firstName, String lastName, String email, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Create the new class from User.
     *
     * @param user          user entity
     * @return              class from user
     */
    public static UserResponse formUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getLastName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().toString()
        );
    }
}
