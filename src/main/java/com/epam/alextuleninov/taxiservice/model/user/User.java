package com.epam.alextuleninov.taxiservice.model.user;

import com.epam.alextuleninov.taxiservice.model.user.role.Role;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Class for User entity.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class User implements Comparable {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Role role;

    public User() {
    }

    public User(long id, String firstName, String lastName,
                String email, String password, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return this.getEmail().compareTo(((User)o).getEmail());
    }
}
